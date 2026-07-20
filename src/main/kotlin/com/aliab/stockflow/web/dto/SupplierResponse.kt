package com.aliab.stockflow.web.dto

import com.aliab.stockflow.domain.Supplier
import java.time.Instant

data class SupplierResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String?,
    val country: String?,
    val createdAt: Instant,
)

fun Supplier.toResponse(): SupplierResponse =
    SupplierResponse(
        id = id,
        name = name,
        email = email,
        phone = phone,
        country = country,
        createdAt = createdAt,
    )
