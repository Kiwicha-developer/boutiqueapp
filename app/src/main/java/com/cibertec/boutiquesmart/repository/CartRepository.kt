package com.cibertec.boutiquesmart.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.cibertec.boutiquesmart.model.Cart
import com.cibertec.boutiquesmart.model.Category
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CartRepository(private val dbHelper: SQLiteOpenHelper) {
    fun addToCart(userId: Int, productId: Int): Long {
        val db = dbHelper.writableDatabase

        // Verificar si ya existe el producto en el carrito del usuario
        val cursor = db.rawQuery(
            "SELECT cantidad FROM cart WHERE user_id = ? AND product_id = ?",
            arrayOf(userId.toString(), productId.toString())
        )

        return if (cursor.moveToFirst()) {
            // Ya existe, actualizamos la cantidad
            val currentQuantity = cursor.getInt(0)
            cursor.close()

            val newValues = ContentValues().apply {
                put("cantidad", currentQuantity + 1)
            }

            db.update(
                "cart",
                newValues,
                "user_id = ? AND product_id = ?",
                arrayOf(userId.toString(), productId.toString())
            )
            2L  // o puedes retornar otro valor si deseas indicar "actualización"
        } else {
            // No existe, insertar nuevo con cantidad 1
            cursor.close()
            val values = ContentValues().apply {
                put("user_id", userId)
                put("product_id", productId)
                put("cantidad", 1)
            }
            db.insert("cart", null, values)
        }
    }

    fun removeFromCart(userId: Int, productId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            "cart",
            "user_id = ? AND product_id = ?",
            arrayOf(userId.toString(), productId.toString())
        )
    }

    fun getCartByUserId(userId: Int): MutableList<Cart> {
        val db = dbHelper.readableDatabase
        val cartItems = mutableListOf<Cart>()

        val query = """
        SELECT 
            u.id AS user_id, u.nombre, u.documento, u.username, u.email, 
            u.password, u.address, u.cardNumber, u.cardVencimiento,u.cardCVV,
            p.id AS product_id, p.name AS product_name, p.category_id, 
            c.name AS category_name, p.color, p.price, p.image, 
            p.stock_s, p.stock_m, p.stock_x,
            ct.cantidad
        FROM cart ct
        JOIN users u ON ct.user_id = u.id
        JOIN products p ON ct.product_id = p.id
        JOIN categories c ON p.category_id = c.id
        WHERE ct.user_id = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        cursor.use {
            while (it.moveToNext()) {
                val user = User(
                    id = it.getInt(it.getColumnIndexOrThrow("user_id")),
                    nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                    documento = it.getString(it.getColumnIndexOrThrow("documento")),
                    username = it.getString(it.getColumnIndexOrThrow("username")),
                    email = it.getString(it.getColumnIndexOrThrow("email")),
                    password = it.getString(it.getColumnIndexOrThrow("password")),
                    address = it.getString(it.getColumnIndexOrThrow("address")),
                    cardNumber = it.getString(it.getColumnIndexOrThrow("cardNumber")),
                    cardVencimiento = it.getString(it.getColumnIndexOrThrow("cardVencimiento")),
                    cardCVV = it.getString(it.getColumnIndexOrThrow("cardCVV"))
                )

                val category = Category(
                    id = it.getInt(it.getColumnIndexOrThrow("category_id")),
                    name = it.getString(it.getColumnIndexOrThrow("category_name"))
                )

                val product = Product(
                    id = it.getInt(it.getColumnIndexOrThrow("product_id")),
                    name = it.getString(it.getColumnIndexOrThrow("product_name")),
                    category = category,
                    color = it.getString(it.getColumnIndexOrThrow("color")),
                    price = it.getFloat(it.getColumnIndexOrThrow("price")),
                    image = it.getString(it.getColumnIndexOrThrow("image")),
                    stock_s = it.getInt(it.getColumnIndexOrThrow("stock_s")),
                    stock_m = it.getInt(it.getColumnIndexOrThrow("stock_m")),
                    stock_x = it.getInt(it.getColumnIndexOrThrow("stock_x"))
                )

                val cantidad = it.getInt(it.getColumnIndexOrThrow("cantidad"))

                cartItems.add(Cart(user = user, product = product, cantidad = cantidad))
            }
        }

        return cartItems
    }

    fun updateQuantity(userId: Int, productId: Int, newQuantity: Int): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("cantidad", newQuantity)
        }
        return db.update("cart", values, "user_id = ? AND product_id = ?", arrayOf(userId.toString(), productId.toString()))
    }


}

