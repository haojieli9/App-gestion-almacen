package com.li.almacen.kt

data class Almacenes(
    val id: String,
    val nombre: String,
    val ubicacion: String
)

data class Articulos(
    val id: String,
    val nombre: String,
    val precio: Float
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
    Articulos("SQRGR001", "Monster1", 1.45f),
    Articulos("SQRGR002", "Monster2", 1.55f),
    Articulos("SQRGR003", "Monster3", 1.65f),
    Articulos("SQRGR004", "Monster4", 1.75f),
    Articulos("SQRGR005", "Monster5", 1.85f),
    Articulos("SQRGR006", "Monster6", 1.95f),
    Articulos("SQRGR007", "Monster7", 2.05f)
)