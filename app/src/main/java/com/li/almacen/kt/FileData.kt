package com.li.almacen.kt

data class Almacenes(
    val id: String,
    val nombre: String,
    val ubicacion: String
)

data class Articulos(
    val id: String,
    val nombre: String
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

val listaArticulo: MutableList<Articulos> = mutableListOf(
    Articulos("SQRGR001", "Monster"),
    Articulos("SQRGR002", "Monster"),
    Articulos("SQRGR003", "Monster"),
    Articulos("SQRGR004", "Monster"),
    Articulos("SQRGR005", "Monster"),
    Articulos("SQRGR006", "Monster"),

)