package com.cibertec.boutiquesmart.model

import java.util.Locale

class Store(var name: String?) {
    val catalogProduct: MutableList<Product> = mutableListOf()
    val catalogCategory: MutableList<Category> = mutableListOf()
    val listOfUsers: MutableList<RegisteredUser> = mutableListOf()

    fun addCategory(category: Category) {
        this.catalogCategory.add(category)
    }


    fun addProduct(product: Product) {
        this.catalogProduct.add(product)
    }

    fun addUser(user: RegisteredUser) {
        this.listOfUsers.add(user)
    }


    fun isInListOfUsersUsername(username: String): Boolean {
        // retorna true si la coleccion no tiene elementos
        return this.listOfUsers.none { it.getName().lowercase(Locale.getDefault()).contains(username.lowercase(
            Locale.getDefault())) }
    }

    fun isInListOfUsersEmail(email: String): Boolean {
        return this.listOfUsers.none { it.getEmail().lowercase(Locale.getDefault()).contains(email.lowercase(
            Locale.getDefault())) }
    }


    fun getUserName(username: String): RegisteredUser? {
        val possibleUser = this.listOfUsers.filter{ it.getName() == username }
        return try {
            possibleUser[0]
        } catch(e: Exception) {
            null
        }
    }

    fun getProduct(id: Int?): Product? {
        val product = this.catalogProduct.filter{ it.idProduct == id }
        return try {
            product[0]
        } catch(e: Exception) {
            null
        }
    }

    fun searchProduct(productName : String) : List<Product> {
        val result = this.catalogProduct.filter { it.name.lowercase(Locale.getDefault()).contains(productName.lowercase(
            Locale.getDefault())) }
        return result
    }
}