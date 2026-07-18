# P06 — Polish: README, seed data, optional API key, pin the repo

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

## Objective
Make the repo the thing a Deloitte reviewer clicks and immediately trusts.

## Tasks
1. README.md: one-paragraph pitch; the honesty line ("Built to deepen my JVM backend skills
   alongside my production Python/FastAPI work"); architecture sketch (simple ASCII or mermaid);
   live URL + Swagger link; local quickstart (compose up, bootRun, test); tech stack list;
   a short "Design decisions" section — money as Long, optimistic locking, price snapshots,
   DTO boundary. Those four bullets are interview bait: each is a story he can tell.
2. Seed data: a `data-local.sql` or CommandLineRunner (local profile only) so the demo isn't empty;
   3 suppliers, ~10 products, a couple of orders.
3. Optional stretch (only if ≥1 spare day): a simple API-key filter (X-API-Key from env) on
   mutating endpoints; document it in README. Skip cleanly if time is short — say so in BUILD_LOG.
4. ktlint clean, no TODOs left, delete dead code.
5. GitHub: pin StockFlow, RegOps, Ajamlugg on the profile; add repo description + topics
   (kotlin, spring-boot, cloud-run, testcontainers).
6. Final BUILD_LOG entry + tick P06.

## Acceptance criteria
- A stranger can go from git clone to running tests in under 5 minutes using only the README.
- The live URL in the README works.

## THEN — and only then
Open the kit's `CV_UPDATE_SNIPPETS.md` and update the CV (or send the snippets + live URL to
Claude in the chat). If ≥2 spare days remain before the deadline, proceed to the frontend kit
(`frontend/AGENTS.md`, phases F00–F04). Otherwise: apply. A deployed, tested backend is enough.
