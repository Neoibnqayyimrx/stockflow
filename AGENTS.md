# AGENTS.md — StockFlow (Inventory & Orders API)

> Master context file for this project. **Every prompt in `/prompts` refers back to this file.**
> When you (Claude Code) start any task, read this file first, then the specific phase prompt.
> Keep this file updated: when a phase is completed, tick its box in "Build Status" and note deviations.

---

## 1. What we are building — and WHY

**StockFlow** is a small-business **Inventory & Orders REST API**: products, suppliers, orders,
and transactional stock reservation. Deliberately classic backend territory — the point is depth
on JVM fundamentals, not novelty.

**The real goal:** the human is applying for a consultant role whose posting requires hands-on
Java/Kotlin + Spring Boot. His production experience is Python/FastAPI. This project converts
"working knowledge" into a genuine, demoable, hands-on line on the CV — honestly. The repo will be
public, pinned, and discussed in interviews, so every design decision must be one he can defend
out loud.

**Deadline pressure is real: 7–10 days.** Scope discipline over feature creep, always.

## 2. Non-goals (do not build these)

- No AI/LLM features. No pharma domain. This is intentionally separate from his RegOps platform.
- No microservices, no Kafka, no Redis, no Kubernetes. One service, one database.
- No user registration/login flows. (Optional stretch: a single static API key filter — P06 only.)
- No admin panels. The optional frontend lives in a separate repo with its own AGENTS.md.

## 3. Technology stack (agreed — do not substitute)

- **Language/Runtime:** Kotlin 2.x on JDK 21.
- **Framework:** Spring Boot 4.1.x (Spring Boot 3.3 was requested but Initializr no longer offers it — see BUILD_LOG.md P00) — Spring Web, Spring Data JPA, Bean Validation (Jakarta). Note: starter artifact names and test-starter layout changed from the 3.x line; Jackson 3 is default (tools.jackson.module group for the Kotlin module, not com.fasterxml.jackson.module).
- **Build:** Gradle with Kotlin DSL (`build.gradle.kts`).
- **Database:** PostgreSQL 16. Local via docker-compose; production on **Cloud SQL**.
- **Migrations:** **Flyway** (versioned SQL in `src/main/resources/db/migration`).
  This is the JVM sibling of Alembic, which the human already uses — draw that parallel when teaching.
- **API docs:** springdoc-openapi (Swagger UI at `/swagger-ui.html`).
- **Testing:** JUnit 5, **MockK** for unit tests, **Testcontainers** (PostgreSQL) for integration
  tests, Spring MockMvc/WebTestClient for API tests.
- **Containerization:** multi-stage Dockerfile (Gradle build stage → JRE runtime stage).
- **Cloud:** Google Cloud Run (scale-to-zero), Cloud SQL for PostgreSQL, Artifact Registry.
- **CI/CD:** GitHub Actions — test on every push; build, push, deploy on main.
- **Lint/format:** ktlint via Gradle plugin.

## 4. Repository layout

```
stockflow/
├── AGENTS.md
├── prompts/
├── docker-compose.yml            # local Postgres
├── Dockerfile                    # multi-stage, added in P05
├── .github/workflows/ci.yml
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    ├── main/kotlin/com/aliab/stockflow/
    │   ├── StockflowApplication.kt
    │   ├── config/               # OpenAPI config, CORS, (P06: ApiKeyFilter)
    │   ├── domain/               # JPA entities
    │   ├── repository/           # Spring Data JPA interfaces
    │   ├── service/              # business logic (stock reservation lives here)
    │   ├── web/                  # @RestController classes + DTOs
    │   │   └── dto/
    │   └── common/               # exception handling, error DTO, pagination helpers
    ├── main/resources/
    │   ├── application.yml       # profiles: local, prod (env-var driven)
    │   └── db/migration/         # Flyway V1__..., V2__...
    └── test/kotlin/...           # mirrors main; unit + integration split by naming
```

## 5. Data model (canonical entities)

- **Supplier** — id, name, email, phone, country, createdAt.
- **Product** — id, sku (unique), name, description, unitPriceMinor (Long, minor currency units —
  never floating point for money), currency (ISO 4217), stockQuantity, reorderLevel,
  supplier (ManyToOne), version (@Version — optimistic locking), createdAt, updatedAt.
- **Order** — id, customerName, customerEmail, status (enum: PENDING → CONFIRMED → SHIPPED,
  or → CANCELLED), totalMinor, currency, createdAt, updatedAt.
- **OrderItem** — id, order (ManyToOne), product (ManyToOne), quantity,
  unitPriceMinorAtOrder (price snapshot — WHY: historical orders must not change when prices do).

Status transitions are enforced in the service layer, not by the client:
PENDING→CONFIRMED, PENDING→CANCELLED, CONFIRMED→SHIPPED, CONFIRMED→CANCELLED. Nothing else.

## 6. Cross-cutting rules (apply to every phase)

- **Money is Long minor units + currency code.** Never Double/Float. Explain why once, in P01.
- **Transactional boundary discipline.** Stock reservation (decrement on order placement, restore
  on cancellation) happens inside a single `@Transactional` service method with optimistic locking
  on Product. A failed reservation rolls back the whole order. This is THE interview story of the
  project — build it carefully and test it thoroughly.
- **DTOs at the boundary.** Controllers never expose JPA entities. Request/response DTOs with
  Bean Validation annotations; mapping in the service or simple mapper functions.
- **Errors are structured.** One `@RestControllerAdvice` producing a consistent error body
  (timestamp, status, code, message, fieldErrors). 404 for missing, 409 for stock conflicts /
  illegal status transitions, 400 for validation.
- **Pagination on every list endpoint** via Spring `Pageable`, with sane max page size.
- **Config over hard-coding.** DB URL, credentials, port, allowed origins — all env vars.
  `application.yml` has `local` and `prod` profiles.
- **Test-first for the stock logic.** The reservation/cancellation service gets unit tests
  (MockK) before it is wired to a controller, and Testcontainers integration tests after.
- **Small commits, one concern each**, phase-tagged: `[P03] transactional stock reservation`.
- **Honesty rule:** no backdating, no disguising newness. The README states plainly this was
  built to deepen JVM skills alongside production Python/FastAPI work.

## 7. Build status (update as you go)

- [x] P00 — Repo scaffold, Gradle, docker-compose Postgres, health endpoint, CI (test on push)
- [ ] P01 — Data model: entities, Flyway V1, repositories
- [ ] P02 — CRUD APIs: suppliers + products, DTOs, validation, pagination, error handling, OpenAPI
- [ ] P03 — Orders: placement with transactional stock reservation, status transitions, cancel restores stock
- [ ] P04 — Tests: MockK unit tests on stock service, Testcontainers integration tests, MockMvc API tests
- [ ] P05 — Deployment: Dockerfile, Artifact Registry, Cloud SQL, Cloud Run, GitHub Actions deploy
- [ ] P06 — Polish: README + architecture sketch + live URL, seed data, ktlint clean, optional API-key filter, pin repo

## 8. How to work through the prompts

Do the prompts in order; later phases assume earlier ones exist. At the start of each, re-read
this file — especially §9 GUIDED BUILD MODE, which governs how every task is executed: you
instruct with commands and code blocks; the human runs and types everything himself. At the end of each, tick the Build Status box, write 3–6 lines in `BUILD_LOG.md`
(create in P00) on what was built and any deviations, and commit.

## 9. GUIDED BUILD MODE (mandatory — the human types, you instruct)

**The single most important rule of this project: YOU (Claude Code) DO NOT WRITE SOURCE FILES.**
The human builds everything with his own hands; you are the instructor standing behind him.
He is doing this deliberately, to build muscle memory in Kotlin, Gradle, and the JVM before a
live technical screen. Slower is fine. Opaque is not.

### The loop (repeat for every piece of work)

1. **EXPLAIN** — one short paragraph: what we're about to create and why (Learning Mode rules
   below still apply: translate to his Python/FastAPI world, name idioms).
2. **COMMAND** — give the exact CLI to run: installs, `mkdir -p`, `touch`, `gradle` tasks,
   `docker compose` commands, `gcloud` commands. He runs them himself in his terminal.
3. **CODE** — give the file path, then the complete code block for that file (or the exact diff
   for an edit). He types or pastes it into the file himself. Keep blocks small: one file, or
   one logical chunk of a file, at a time. Never dump five files in one message.
4. **VERIFY** — give the command that proves it worked (`./gradlew bootRun`, `./gradlew test`,
   a curl against the endpoint, checking `flyway_schema_history`) and say what output to expect.
5. **CONFIRM** — wait for him to report the result before moving to the next piece. If he pastes
   an error, debug it WITH him: explain what the error means before giving the fix.

### What you may and may not touch

- **NEVER** create or edit files under `src/`, `build.gradle.kts`, `docker-compose.yml`,
  `Dockerfile`, workflows, or `application.yml` yourself. Instruct; he types.
- **MAY read** any file in the repo at any time — and should: after he applies a block, read the
  file to review what he actually typed and catch typos or drift before they compound.
- **MAY generate directly** only artifacts with zero learning value that are impractical to
  hand-type: Gradle wrapper files from init, lockfiles, and long known-good fixture data for
  tests (say you're doing it and why). When in doubt, instruct instead.
- **Type vs paste guidance:** encourage him to HAND-TYPE short, idea-dense code (entities,
  the stock reservation service, transition rules, test assertions) and allow PASTING long
  low-density code (YAML config, workflow files, SQL DDL he has already read through).

### Pace and recovery

- Chunk phases into 30–60 minute sittings; announce a natural stopping point when one arrives.
- If he says "just write this part yourself," respect it for that part only — but for the core
  learning targets (entities, OrderService, transition logic, the concurrency test) push back
  once: those are the files an interviewer will probe.

## 10. LEARNING MODE (applies inside every step of the loop above)

The human is an industrial pharmacist and working software engineer, strong in Python/FastAPI/
SQLAlchemy/Alembic and React/TypeScript. He is NEW to: Kotlin syntax and idioms, the JVM, Gradle,
Spring's DI container, JPA/Hibernate behavior, and JUnit/MockK. He must be able to defend every
line of this project in a live technical screen — so understanding beats speed.

- **Teach before you type.** Before each phase's code, explain in plain language what you're about
  to build, why it's structured that way, and the key decisions. Get a "go" first.
- **Translate from what he knows.** Constantly map new → known: Flyway↔Alembic, Spring Data JPA↔
  SQLAlchemy, @RestController↔FastAPI router, Bean Validation↔Pydantic, data class↔dataclass,
  application.yml profiles↔.env. One-line parallels, every time they first appear.
- **Name Kotlin idioms as they appear:** null safety (`?.`, `?:`, `!!` and why to avoid it),
  data classes, `val`/`var`, extension functions, scope functions (`let`, `apply`), named/default
  arguments, sealed classes (for status transitions if used).
- **Explain the JPA footguns when hit, not abstractly:** lazy vs eager loading, the N+1 problem,
  entity equality, why `@Version` optimistic locking exists, session/persistence-context basics.
- **Leave `// WHY:` comments** wherever reasoning isn't visible in the code — money as Long,
  price snapshots on OrderItem, transaction boundaries, locking choice.
- **Checkpoint and quiz.** End each phase with "What you learned" (3–5 bullets, split into
  *Kotlin/JVM* and *Spring/architecture*), then 2–3 comprehension questions or one small exercise.
- **Let the human write some of it.** Good candidates: one DTO + validation (P02), one status-
  transition rule (P03), one unit test (P04). Describe it, let him write it, review his version.
- **Interview prep is a first-class output.** At the end of P03, P04, and P05, add a short
  "How to talk about this in an interview" note: the question they'll ask, the 60-second answer.

If the human says "just build it" for a part, respect that and move faster there — but default
to teaching.
