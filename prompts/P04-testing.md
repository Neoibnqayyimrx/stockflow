# P04 — Tests: MockK units, Testcontainers integration, MockMvc API

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

> Read `AGENTS.md` §6 "Test-first for the stock logic".

## Objective
A test suite that proves the stock logic, runs the same in CI as locally, and is
disproportionately impressive on a junior-level repo (Testcontainers is the star).

## Teach first
- The JVM test stack mapped to his world: JUnit 5 ↔ pytest, MockK ↔ unittest.mock/pytest-mock,
  Testcontainers ↔ "pytest fixture that boots a real throwaway Postgres in Docker".
- Unit vs integration split here: units mock repositories and test pure business rules fast;
  integration tests use the real DB because locking and constraints only exist there.

## Tasks
1. Unit tests (MockK) for OrderService: happy path totals + snapshots; insufficient stock throws
   and saves nothing; every legal/illegal status transition; cancel restores stock.
2. Testcontainers base: singleton Postgres container + Flyway migrations applied; explain the
   singleton-container pattern (speed).
3. Integration tests: place order really decrements; the CONCURRENCY test — two threads order
   the last unit, assert exactly one success and final stock 0 (this test IS the interview proof);
   DB CHECK constraint holds if service validation were bypassed.
4. MockMvc API tests: POST /api/orders happy + 409 body shape; validation 400 shape; pagination.
5. Wire into CI (Docker is available on GitHub runners; note Testcontainers just works there).
6. Human exercise: HE writes the cancel-restores-stock unit test first, you review, then together
   write the concurrency test.

## Acceptance criteria
- `./gradlew test` green locally and in CI; concurrency test passes repeatably (run it 5×).
- Coverage of service layer is high; do NOT chase 100% on DTOs/config.

## Interview note
"How do you know your code works?" → the concurrency Testcontainers test, described in 60 seconds.

## Checkpoint
Recap + quiz. Tick P04. Commits: `[P04] unit tests order service`, `[P04] testcontainers + concurrency test`.
