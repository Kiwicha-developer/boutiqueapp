package com.cibertec.boutiquesmart.model

data class User (
    var id: Int,
    var username: String,
    var email: String,
    var password: String,
    var address: String,
    var payment: String,
    var typePayment: String
)