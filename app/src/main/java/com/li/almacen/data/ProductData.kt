package com.li.almacen.data

import java.util.Date

data class ProductData(
    var id: String? = null,
    val name: String? = null,
    val barcode: String? = null,
    val almacenDestino: String? = null,
    val categoria: String? = null,
    val proveedor: String? = null,
    val cantidad: String? = null,
    val coste: String? = null,
    val venta:String? = null,
    val descriptor: String? = null,
    val fechaVencimiento: String? = null,
    val uri : Any? = null,
    val fechaCreacion: Date = Date()
)
