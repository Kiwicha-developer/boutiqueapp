package com.cibertec.boutiquesmart.model

data class Order(
    val id: Int,
    val products: List<Product>,
    val total: Float,
    val address: String)