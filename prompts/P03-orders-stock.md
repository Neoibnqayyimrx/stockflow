# P03 — Orders: transactional stock reservation (THE interview story)

> **MODE: GUIDED BUILD (AGENTS.md §9).** Do not write any source files yourself. For every task below: explain → give CLI commands → give the file path + code block for the human to type/paste → give a verification command → wait for his result before continuing. Review files after he applies changes.

> Read `AGENTS.md` §6 "Transactional boundary discipline". Teach carefully — this phase is the
> single most important thing the human will discuss in interviews.

## Objective
Order placement that atomically reserves stock across all items; status transitions enforced
server-side; cancellation restores stock. Correct under concurrency.

## Teach first (do not skip)
- What `@Transactional` actually does in Spring (proxy, begin/commit/rollback), and the classic
  self-invocation footgun (calling a @Transactional method from the same class bypasses the proxy).
- Optimistic (@Version) vs pessimistic (SELECT ... FOR UPDATE) locking: tradeoffs, and why we
  choose optimistic + retry-or-fail for this scale. Relate to race conditions he may know from
  Python async.
- Why the DB CHECK (stock >= 0) from P01 is the last line of defense.

## Tasks
1. `OrderService.placeOrder(request)` — single @Transactional method:
   load products, validate requested quantities against stock, decrement stock, snapshot
   unit prices into OrderItems, compute totalMinor in code, save Order(PENDING).
   Any shortfall → custom `InsufficientStockException` → rollback everything → 409 with a body
   listing which SKUs are short.
2. Status transitions per AGENTS.md §5, enforced in the service (illegal transition → 409).
   Consider a sealed-class or enum-with-allowedNext design; name the pattern.
3. `cancelOrder(id)`: only from PENDING/CONFIRMED; restores stock inside the same transaction.
4. Endpoints: POST `/api/orders`, GET `/api/orders` (paginated, `?status=` filter),
   GET `/api/orders/{id}` (with items), POST `/api/orders/{id}/confirm|ship|cancel`.
5. OptimisticLockingFailureException → 409 with a "please retry" error code; explain why we
   surface it rather than silently retrying (small scale, honest semantics).
6. Human exercise: HE implements one transition rule (e.g. CONFIRMED→SHIPPED) + its error path.

## Acceptance criteria
- Placing an order for more than available stock changes NOTHING in the DB (verify).
- Two rapid concurrent orders for the last unit: exactly one succeeds (manual check now;
  proven properly by a Testcontainers test in P04).
- Cancelling a CONFIRMED order restores stock exactly; SHIPPED cannot be cancelled.

## Interview note (write it at the end of the phase)
The 60-second answer to "tell me about a technical challenge": stock reservation under
concurrency — transaction boundary, optimistic locking, rollback semantics, DB-level check as
defense in depth, and the test that proves it.

## Checkpoint
Recap + quiz (what happens if placeOrder calls another @Transactional method on `this`?
why snapshot prices?). Tick P03. Commits: `[P03] transactional stock reservation`, etc.
