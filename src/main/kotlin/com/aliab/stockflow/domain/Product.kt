
package com.aliab.stockflow.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant

@Entity
@Table(name = "product")
class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "sku", nullable = false, unique = true)
    var sku: String = "",

    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "description")
    var description: String? = null,

    // WHY: money is stored as minor units (e.g. cents) in a Long, never Double/Float —
    // binary floating point cannot represent decimal fractions exactly, which silently
    // corrupts monetary sums over time.
    @Column(name = "unit_price_minor", nullable = false)
    var unitPriceMinor: Long = 0,

    @Column(name = "currency", nullable = false)
    var currency: String = "",

    @Column(name = "stock_quantity", nullable = false)
    var stockQuantity: Int = 0,

    @Column(name = "reorder_level", nullable = false)
    var reorderLevel: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    var supplier: Supplier,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now(),

    // WHY: optimistic locking — Hibernate includes `WHERE version = ?` on every UPDATE
    // and auto-increments this column. Two concurrent updates to the same row means the
    // second one's WHERE clause matches nothing, and Hibernate throws instead of silently
    // overwriting. Critical for concurrent stock decrements (full story in P03).
    @Version
    @Column(name = "version", nullable = false)
    var version: Long = 0,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
