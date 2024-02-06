package com.li.almacen

data class Almacenes(
    val id: String,
    val nombre: String,
    val ubicacion: String
)


val listaAlmacen: MutableList<Almacenes> = mutableListOf(
    Almacenes("SQRGR001", "Almacen principal", "Av. Gregorio Gea"),
    Almacenes("SQRGR002", "Almacen respaldo", "Av. Gregorio Gea"),
    Almacenes("SQRGR003", "Almacen tercero", "Av. Gregorio Gea"),
    Almacenes("SQRGR003", "Almacen tercero", "Av. Gregorio Gea"),
    Almacenes("SQRGR003", "Almacen tercero", "Av. Gregorio Gea"),
    Almacenes("SQRGR003", "Almacen tercero", "Av. Gregorio Gea"),
    Almacenes("SQRGR003", "Almacen tercero", "Av. Gregorio Gea")
    )