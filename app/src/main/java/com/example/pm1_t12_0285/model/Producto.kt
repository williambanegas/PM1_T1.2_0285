package com.example.pm1_t12_0285.model

data class Producto(
    val id: Long = 0,
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val codigoBarra: String,
    val stockMinimo: Int
)
