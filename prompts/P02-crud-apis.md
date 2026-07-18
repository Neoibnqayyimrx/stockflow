# P02 — CRUD APIs: suppliers + products

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

> Read `AGENTS.md` §6. Teach before you type.

## Objective
Full CRUD for Supplier and Product with DTOs, Bean Validation, pagination, structured errors,
and OpenAPI docs.

## Teach first
- @RestController ↔ FastAPI router; DTO + Bean Validation ↔ Pydantic schema. Map the mental model.
- Why controllers never return entities (lazy-loading serialization traps, API stability).
- Spring `Pageable`/`Page<T>`: what the client sends, what comes back.

## Tasks
1. DTOs in `web/dto/`: CreateSupplierRequest, SupplierResponse, CreateProductRequest,
   UpdateProductRequest, ProductResponse. Jakarta validation annotations (@NotBlank, @Email,
   @Positive, @Size...). Kotlin note: annotations on constructor params need use-site targets
   (`@field:NotBlank`) — name and explain this idiom the first time.
2. Endpoints: POST/GET/GET{id}/PUT/DELETE for `/api/suppliers` and `/api/products`.
   List endpoints paginated (`Pageable`, max size 100) with simple filtering
   (products: `?supplierId=`, `?belowReorderLevel=true`).
3. `common/`: `@RestControllerAdvice` → error body {timestamp, status, code, message, fieldErrors[]}.
   Handle: MethodArgumentNotValidException (400), EntityNotFound (404), DataIntegrityViolation
   (409, e.g. duplicate SKU).
4. OpenAPI: annotate enough that Swagger UI reads clean; set a proper API title/version.
5. Human exercise: HE writes CreateSupplierRequest + its validation and the POST endpoint;
   you review and refine together.

## Acceptance criteria
- All CRUD verbs verified via Swagger UI against local Postgres.
- Invalid create returns 400 with fieldErrors listing each violation; duplicate SKU → 409.
- Deleting a supplier that still has products → 409 (explain FK behavior), not a 500.

## Checkpoint
Recap + quiz (why @field: target? what would leak if we serialized the entity directly?).
Tick P02. Commit per concern: `[P02] supplier CRUD`, `[P02] product CRUD`, `[P02] error advice`.
