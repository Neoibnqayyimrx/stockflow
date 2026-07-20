package com.aliab.stockflow.repository

import com.aliab.stockflow.domain.Supplier
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierRepository : JpaRepository<Supplier, Long>
