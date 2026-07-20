package com.aliab.stockflow.web

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import jakarta.persistence.EntityNotFoundException

import com.aliab.stockflow.domain.Supplier
import com.aliab.stockflow.repository.SupplierRepository
import com.aliab.stockflow.web.dto.CreateSupplierRequest
import com.aliab.stockflow.web.dto.SupplierResponse
import com.aliab.stockflow.web.dto.toResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/suppliers")
class SupplierController(
    private val repository: SupplierRepository,
) {

    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateSupplierRequest,
    ): ResponseEntity<SupplierResponse> {
        val supplier = Supplier(
            name = request.name,
            email = request.email,
            phone = request.phone,
            country = request.country,
        )
        val saved = repository.save(supplier)
        return ResponseEntity.status(201).body(saved.toResponse())
    }


        @GetMapping
    fun list(pageable: Pageable): Page<SupplierResponse> =
        repository.findAll(pageable).map { it.toResponse() }

    @GetMapping("/{id}")
    fun getOne(
        @PathVariable id: Long,
    ): SupplierResponse =
        repository.findById(id)
            .orElseThrow { EntityNotFoundException("Supplier $id not found") }
            .toResponse()

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateSupplierRequest,
    ): SupplierResponse {
        val supplier = repository.findById(id)
            .orElseThrow { EntityNotFoundException("Supplier $id not found") }
        supplier.name = request.name
        supplier.email = request.email
        supplier.phone = request.phone
        supplier.country = request.country
        return repository.save(supplier).toResponse()
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<Void> {
        if (!repository.existsById(id)) {
            throw EntityNotFoundException("Supplier $id not found")
        }
        repository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

}