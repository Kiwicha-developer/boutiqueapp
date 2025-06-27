package com.cibertec.boutiquesmart.model

class RegisteredUser (
    val idUser: Int,
    private var name: String,
    private var email: String,
    private var password: String) {

    private var address = ""
    private var debitCard = ""
    private var creditCard = ""
    private var shoppingCart = mutableListOf<Product>()
    private var favorites = mutableListOf<Product>()
    private var orders = mutableListOf<Order>()

    // GETTERS
    fun getName(): String {
        return this.name
    }

    fun getEmail(): String {
        return this.email
    }

    fun getPassword(): String {
        return this.password
    }

    fun getAddress(): String {
        return this.address
    }

    fun getDebitCard(): String {
        return this.debitCard
    }

    fun getCreditCard(): String {
        return this.creditCard
    }

    fun getShoppingCart(): MutableList<Product> {
        return this.shoppingCart
    }

    fun getFavorites(): MutableList<Product> {
        return this.favorites
    }

    fun getOrders(): MutableList<Order> {
        return this.orders
    }

    fun getTotal(): Float {
        var total = 0F
        this.shoppingCart.forEach {
            total += it.price
        }
        return total
    }

    // SETTERS
    fun setName(name: String) {
        this.name = name
    }

    fun setEmail(email:String) {
        this.email = email
    }

    fun setPassword(password:String) {
        this.password= password
    }

    fun setAddress(address: String) {
        this.address = address
    }

    fun setCreditCard(creditCard: String) {
        this.creditCard = creditCard
    }

    fun setDebitCard(debitCard: String) {
        this.debitCard = debitCard
    }

    fun setShoppingCart(shoppingCart: MutableList<Product>) {
        this.shoppingCart = shoppingCart
    }

    fun addToCart(product: Product) {
        this.shoppingCart.add(product)
    }

    fun removeFromCart(product: Product) {
        this.shoppingCart.remove(product)
    }

    fun addToFavorite(product: Product) {
        this.favorites.add(product)
    }

    fun addOrder(order: Order) {
        this.orders.add(order)
    }
}