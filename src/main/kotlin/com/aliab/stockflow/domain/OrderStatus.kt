package com.aliab.stockflow.domain

// WHY: transitions are documented here for reference but enforced in the service layer
// (OrderService, P03), not by the database or this enum itself. Allowed transitions:
//   PENDING   -> CONFIRMED
//   PENDING   -> CANCELLED
//   CONFIRMED -> SHIPPED
//   CONFIRMED -> CANCELLED
// No other transition is valid (e.g. SHIPPED is terminal, CANCELLED is terminal).
enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    CANCELLED,
}
