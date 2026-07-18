# P01 — Data model: entities, Flyway V1, repositories

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

> Read `AGENTS.md` §5 (data model) and §6 (cross-cutting rules) first. Teach before you type.

## Objective
JPA entities for Supplier, Product, Order, OrderItem; Flyway `V1__init.sql`; Spring Data
repositories. App boots with migrations applied.

## Teach first
- Flyway ↔ Alembic: versioned, forward-only SQL; why we write SQL by hand instead of
  auto-generating from entities (review-ability — same reason he reviews Alembic autogen).
- JPA entity ↔ SQLAlchemy model; persistence context ↔ session, in one paragraph.
- Why money is `Long` minor units + currency code, never Double. `// WHY:` comment in code too.
- `@Version` optimistic locking on Product: what it is, why stock needs it (full story lands in P03).

## Tasks
1. Write `V1__init.sql`: four tables, FKs, unique constraint on product.sku, sensible NOT NULLs,
   indexes on FKs. CHECK constraint: stock_quantity >= 0 (defense in depth under concurrency).
2. Entities in `domain/` mirroring AGENTS.md §5 exactly, including `@Version` on Product and
   `unitPriceMinorAtOrder` snapshot on OrderItem (`// WHY:` comment).
3. Status enum + allowed transitions documented next to it (enforcement comes in P03).
4. Repositories: `SupplierRepository`, `ProductRepository`, `OrderRepository`
   (JpaRepository). Add one derived query (e.g. `findBySku`) and explain derived-query naming.
5. Entity equality: implement equals/hashCode by id-or-identity correctly; explain the classic
   JPA pitfall in two sentences.

## Acceptance criteria
- Fresh DB + `bootRun` → Flyway applies V1 cleanly; `flyway_schema_history` shows it.
- App boots with no schema-validation warnings (`ddl-auto: validate`).
- Human exercise: HE writes the Supplier entity himself from the SQL; you review it.

## Checkpoint
Recap + quiz (why Long for money? what does @Version change in the UPDATE statement?).
Tick P01. Commit: `[P01] entities, flyway V1, repositories`.
