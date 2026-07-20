package com.aliab.stockflow.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateSupplierRequest(

    @field:NotBlank
    @field:Size(max = 255)
    val name: String,

    @field:NotBlank
    @field:Email
    @field:Size(max = 255)
    val email: String,

    @field:Size(max = 50)
    val phone: String? = null,

    @field:Size(max = 100)
    val country: String? = null,
)