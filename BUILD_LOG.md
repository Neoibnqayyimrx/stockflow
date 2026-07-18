# Build Log

## P00 — Repo scaffold, Gradle, docker-compose Postgres, CI
- What was scaffolded: Spring Boot project via the Initializr API (curl) with web, data-jpa, validation, postgresql, flyway, and actuator starters; Postgres 16 running via docker-compose; health check green at /actuator/health.
- Key versions: Kotlin 2.3.21, Spring Boot 4.1.0, JDK 21, Postgres 16
- Deviation: Spring Boot 3.3 requested but Initializr requires >=4.0 — went with 4.1.0
- One thing that surprised me coming from Python/FastAPI tooling: how much Spring Boot configures itself from the classpath alone — adding the postgres driver and data-jpa starter produced a working DataSource with zero wiring code, where FastAPI would need me to build the engine and session explicitly. Also: one tool (Gradle) doing the job of pip, a Makefile, and a test runner combined.