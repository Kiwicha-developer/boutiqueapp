package com.cibertec.boutiquesmart.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import com.cibertec.boutiquesmart.model.Category
import com.cibertec.boutiquesmart.model.Product

class FavoritesRepository(private val dbHelper: SQLiteOpenHelper) {

    fun addToFavorites(userId: Int, productId: Int): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("product_id", productId)
        }
        return db.insert("favorites", null, values)
    }

    fun removeFromFavorites(userId: Int, productId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            "favorites",
            "user_id = ? AND product_id = ?",
            arrayOf(userId.toString(), productId.toString())
        )
    }

    fun getFavoritesByUserId(userId: Int): List<Product> {
        val db = dbHelper.readableDatabase
        val favorites = mutableListOf<Product>()

        val query = """
            SELECT p.id, p.name, p.category_id, c.name AS category_name,
                   p.color, p.price, p.image, p.stock_s, p.stock_m, p.stock_x
            FROM favorites f
            JOIN products p ON f.product_id = p.id
            JOIN categories c ON p.category_id = c.id
            WHERE f.user_id = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        cursor.use {
            while (it.moveToNext()) {
                val category = Category(
                    id = it.getInt(it.getColumnIndexOrThrow("category_id")),
                    name = it.getString(it.getColumnIndexOrThrow("category_name"))
                )
                val product = Product(
                    id = it.getInt(it.getColumnIndexOrThrow("id")),
                    name = it.getString(it.getColumnIndexOrThrow("name")),
                    category = category,
                    color = it.getString(it.getColumnIndexOrThrow("color")),
                    price = it.getFloat(it.getColumnIndexOrThrow("price")),
                    image = it.getString(it.getColumnIndexOrThrow("image")),
                    stock_s = it.getInt(it.getColumnIndexOrThrow("stock_s")),
                    stock_m = it.getInt(it.getColumnIndexOrThrow("stock_m")),
                    stock_x = it.getInt(it.getColumnIndexOrThrow("stock_x"))
                )
                favorites.add(product)
            }
        }

        return favorites
    }
}