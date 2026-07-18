# P00 — Repo scaffold, tooling, CI

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

> Read `AGENTS.md` first. Teach before you type (Learning Mode §9).

## Objective
A running Spring Boot app with a health endpoint, local Postgres via docker-compose,
ktlint, and a GitHub Actions workflow that runs tests on every push.

## Teach first (before code)
- What Gradle is vs pip/uv + a task runner; what `build.gradle.kts` and `settings.gradle.kts` do.
- What Spring Boot auto-configuration means; how the app starts (main fn → SpringApplication.run).
- JDK 21 + Kotlin toolchain: what "targeting the JVM" means for a Python dev.

## Tasks
1. Walk the human through generating the project himself — either on start.spring.io (give the exact settings to click) or via a single curl command against the Initializr API (give it ready to run). Settings: Kotlin, Gradle-Kotlin DSL, JDK 21,
   Spring Boot 3.3+, dependencies: web, data-jpa, validation, postgresql, flyway, actuator.
   Group `com.aliab`, artifact `stockflow`.
2. Add springdoc-openapi and ktlint Gradle plugin.
3. `docker-compose.yml`: postgres:16 with a named volume, port 5432, env-var credentials.
4. `application.yml` with `local` profile (compose DB) and `prod` profile (all values from env).
5. Health: rely on actuator `/actuator/health`; confirm it returns UP with the DB connected.
6. `.github/workflows/ci.yml`: on push/PR → set up JDK 21, cache Gradle, `./gradlew ktlintCheck test`.
7. Have him create `BUILD_LOG.md` and write the first entry himself: what was scaffolded, versions chosen, and one thing that surprised him coming from Python tooling.

## Acceptance criteria
- `docker compose up -d` then `./gradlew bootRun` → `/actuator/health` = `{"status":"UP"}`.
- `./gradlew test ktlintCheck` passes locally and in CI on the first push.
- No secrets committed; `.env.example` documents needed vars.

## Checkpoint
"What you learned" (Kotlin/JVM vs Spring split) + 2 quiz questions
(suggested: what does auto-configuration decide from the classpath? where do prod credentials live and why?).
Tick P00 in AGENTS.md Build Status. Commit: `[P00] scaffold, compose postgres, CI`.
