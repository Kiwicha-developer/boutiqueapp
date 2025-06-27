package com.cibertec.boutiquesmart.model

data class Product(
    val idProduct: Int,
    val name: String,
    val category: Category?,
    val color: String,
    val price: Float,
    val image: Int,
    var quantity: Map<String, Int>?
)