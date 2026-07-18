# P05 — Deployment: Docker, Artifact Registry, Cloud SQL, Cloud Run, CI/CD

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

> Read `AGENTS.md` §3 cloud stack. Teach before you type — GCP concepts are a learning goal.

## Objective
The API live on a public Cloud Run URL, backed by Cloud SQL Postgres, deployed automatically
from main via GitHub Actions.

## Teach first
- Cloud Run mental model: container-as-a-service, scale-to-zero, request-scoped billing —
  why it's the cheap right answer here vs GKE/GCE.
- Cloud SQL connection options; we use the Cloud SQL connector / unix socket from Cloud Run.
- Artifact Registry ↔ Docker Hub; Workload Identity Federation vs long-lived JSON keys
  (prefer WIF; if time is short a service-account key is acceptable — say the tradeoff out loud).

## Tasks
1. Multi-stage Dockerfile: gradle:jdk21 build stage → eclipse-temurin:21-jre runtime, non-root
   user, `SPRING_PROFILES_ACTIVE=prod`. Explain layer caching for the Gradle deps.
2. GCP setup (walk the human through console/gcloud, he runs the commands):
   project, enable APIs (run, sqladmin, artifactregistry), Artifact Registry repo,
   smallest Cloud SQL Postgres instance, DB + user. Budget alert at $10 — do not skip this.
3. Prod config: all secrets as Cloud Run env vars (or Secret Manager if time allows);
   Flyway runs on startup against Cloud SQL.
4. Deploy manually once (`gcloud run deploy`) to validate; then
   `.github/workflows/deploy.yml`: on push to main → test → build → push to Artifact Registry →
   deploy to Cloud Run. Keep ci.yml (tests) for PRs.
5. Smoke check: `/actuator/health` on the public URL; create a supplier+product via Swagger on prod.

## Acceptance criteria
- Public URL serves Swagger UI and health UP; a push to main redeploys with zero manual steps.
- No secrets in the repo, ever. Budget alert configured.

## Interview note
Cost-consciousness story (maps to a line in the Deloitte posting): scale-to-zero Cloud Run +
smallest Cloud SQL tier + budget alert = a real "cost-effective engineering" example with numbers.

## Checkpoint
Recap + quiz (why multi-stage? what triggers a cold start and what does it cost you?).
Tick P05. Commits: `[P05] dockerfile`, `[P05] cloud run deploy workflow`.
