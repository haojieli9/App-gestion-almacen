package com.li.almacen.data

import java.util.Date

data class MovementData(
    var id: String? = null,
    val name: String? = null,
    val manager: String? = null,
    val movementType: String? = null,
    val fecha: Date = Date()
)
