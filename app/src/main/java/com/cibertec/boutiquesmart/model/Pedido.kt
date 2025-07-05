package com.cibertec.boutiquesmart.model

data class Pedido(
    val producto: String,
    val cantidad: String,
    val precioUnitario: String,
    val estado: String,
    val fecha: String
)