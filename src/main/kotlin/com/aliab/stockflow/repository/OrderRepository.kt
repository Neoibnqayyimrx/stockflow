package com.aliab.stockflow.repository

import com.aliab.stockflow.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>
