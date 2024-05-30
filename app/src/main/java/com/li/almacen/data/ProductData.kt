package com.li.almacen.data

import java.math.BigDecimal
import java.time.LocalDateTime
/*
data class ProductData(
    var id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val sku: String? = null,
    val barcode: String? = null,
    val categoryId: Int? = null,
    val price: BigDecimal? = null,
    val cost: BigDecimal? = null,
    val active: Boolean = true,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)*/

data class ProductData(
    var id: String? = null,
    val name: String? = null,
    val cantidad: String? = null,
    val price: String? = null,
)