package com.example.pm1_t12_0285.model

data class Movimiento(
    val id: Long = 0,
    val idProducto: Long,
    val nombreProducto: String? = null,
    val cantidad: Int,
    val tipo: String, // "ENTRADA" o "SALIDA"
    val fecha: String
)
