package com.cibertec.boutiquesmart.model

data class User (
    var id: Int,
    var nombre: String,
    var documento: String,
    var username: String,
    var email: String,
    var password: String,
    var address: String,
    var cardNumber: String,
    var cardVencimiento: String,
    var cardCVV: String
)