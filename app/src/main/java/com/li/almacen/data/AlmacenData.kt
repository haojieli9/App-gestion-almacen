package com.li.almacen.data

import java.util.Date

data class AlmacenData(
    var id :String? = "",
    val name :String = "",
    val notas :String = "",
    val gerente :String = "",
    val ubicacion :String = "",
    val uri : Any? = null,
    val fechaCreacion: Date = Date()

)

