package com.li.almacen.kt

data class Almacenes(
    val id: String,
    val nombre: String,
    val ubicacion: String,
    val cover: String
)

data class Articulos(
    val id: String,
    val nombre: String,
    val precio: Float
)

val listaAlmacen: MutableList<Almacenes> = mutableListOf(
    Almacenes("SQRGR001", "Almacen principal", "Av. Gregorio Gea1", "https://loremflickr.com/80/80?lock=1"),
    Almacenes("SQRGR002", "Almacen respaldo", "Av. Gregorio Gea2", "https://loremflickr.com/80/80?lock=2"),
    Almacenes("SQRGR003", "Almacen tercero", "Av. Gregorio Gea3", "https://loremflickr.com/80/80?lock=3"),
    Almacenes("SQRGR004", "Almacen cuarto", "Av. Gregorio Gea4", "https://loremflickr.com/80/80?lock=4"),
    Almacenes("SQRGR005", "Almacen quinto", "Av. Gregorio Gea5","https://loremflickr.com/80/80?lock=5"),
    Almacenes("SQRGR006", "Almacen sexto", "Av. Gregorio Gea6", "https://loremflickr.com/80/80?lock=6"),
    Almacenes("SQRGR007", "Almacen septimo", "Av. Gregorio Gea7", "https://loremflickr.com/80/80?lock=7")
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