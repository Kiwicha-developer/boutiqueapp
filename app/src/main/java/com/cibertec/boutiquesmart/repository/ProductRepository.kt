package com.cibertec.boutiquesmart.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import com.cibertec.boutiquesmart.model.Category
import com.cibertec.boutiquesmart.model.Product

class ProductRepository(private val dbHelper: SQLiteOpenHelper) {
    fun insertProduct(product: Product): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", product.name)
            put("category_id", product.category.id)
            put("color", product.color)
            put("price", product.price)
            put("image", product.image)
            put("stock_s", product.stock_s)
            put("stock_m", product.stock_m)
            put("stock_x", product.stock_x)
        }
        return db.insert("products", null, values)
    }

    fun updateProduct(product: Product): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", product.name)
            put("category_id", product.category.id)
            put("color", product.color)
            put("price", product.price)
            put("image", product.image)
            put("stock_s", product.stock_s)
            put("stock_m", product.stock_m)
            put("stock_x", product.stock_x)
        }
        return db.update("products", values, "id = ?", arrayOf(product.id.toString()))
    }

    fun deleteProduct(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete("products", "id = ?", arrayOf(id.toString()))
    }

    fun getProductById(id: Int): Product? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val product = buildProductFromCursor(cursor)
            cursor.close()
            product
        } else {
            cursor.close()
            null
        }
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM products", null)
        if (cursor.moveToFirst()) {
            do {
                val product = buildProductFromCursor(cursor)
                if (product != null) {
                    products.add(product)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    fun searchProductsByName(query: String): List<Product> {
        val products = mutableListOf<Product>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM products WHERE name LIKE ?",
            arrayOf("%$query%")
        )
        if (cursor.moveToFirst()) {
            do {
                val product = buildProductFromCursor(cursor)
                if (product != null) {
                    products.add(product)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return products
    }

    private fun buildProductFromCursor(cursor: Cursor): Product? {
        val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"))
        val categoryName = getCategoryNameById(categoryId) ?: return null

        return Product(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
            category = Category(categoryId, categoryName),
            color = cursor.getString(cursor.getColumnIndexOrThrow("color")),
            price = cursor.getFloat(cursor.getColumnIndexOrThrow("price")),
            image = cursor.getString(cursor.getColumnIndexOrThrow("image")),
            stock_s = cursor.getInt(cursor.getColumnIndexOrThrow("stock_s")),
            stock_m = cursor.getInt(cursor.getColumnIndexOrThrow("stock_m")),
            stock_x = cursor.getInt(cursor.getColumnIndexOrThrow("stock_x"))
        )
    }

    private fun getCategoryNameById(id: Int): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT name FROM categories WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            cursor.close()
            name
        } else {
            cursor.close()
            null
        }
    }
}