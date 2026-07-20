package com.aliab.stockflow.repository

import com.aliab.stockflow.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findBySku(sku: String): Product?
}
