package com.cibertec.boutiquesmart.model

data class Product (
    val id: Int,
    val name: String,
    val category: Category,
    val color: String,
    val price: Float,
    val image: String,
    val stock_s: Int,
    val stock_m: Int,
    val stock_x: Int
)