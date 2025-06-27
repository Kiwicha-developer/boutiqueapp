package com.cibertec.boutiquesmart.controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.model.Category
import com.cibertec.boutiquesmart.model.Product

class BaseDatos(context: Context) : SQLiteOpenHelper(context, "boutique.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY,
                name TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE products (
                id INTEGER PRIMARY KEY,
                name TEXT,
                category_id INTEGER,
                color TEXT,
                price REAL,
                image INTEGER,
                stock_s INTEGER,
                stock_m INTEGER,
                stock_x INTEGER,
                FOREIGN KEY(category_id) REFERENCES categories(id)
            )
        """)

        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                email TEXT,
                password TEXT
            )
        """)

        db.execSQL("CREATE TABLE cart (user_id INTEGER, product_id INTEGER)")
        db.execSQL("CREATE TABLE favorites (user_id INTEGER, product_id INTEGER)")
        db.execSQL("CREATE TABLE address (user_id INTEGER PRIMARY KEY, address TEXT)")
        db.execSQL("CREATE TABLE payment (user_id INTEGER PRIMARY KEY, debit TEXT, credit TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS products")
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS cart")
        db.execSQL("DROP TABLE IF EXISTS favorites")
        db.execSQL("DROP TABLE IF EXISTS address")
        db.execSQL("DROP TABLE IF EXISTS payment")
        onCreate(db)
    }

    companion object Base {
        fun start(context: Context) {
            context.deleteDatabase("boutique.db") // borra la base anterior
            val db = BaseDatos(context).writableDatabase
            setCategory(db)
            setProducts(db)
            setUsers(db)
            setShoppingCart(db)
            setFavorites(db)
            setAddress(db)
            setPaymentMethod(db)
        }

        private fun setCategory(db: SQLiteDatabase) {
            val categories = listOf("Dama", "Caballero")
            categories.forEachIndexed { index, name ->
                val values = ContentValues().apply {
                    put("id", index + 1)
                    put("name", name)
                }
                db.insert("categories", null, values)
            }
        }

        private fun setProducts(db: SQLiteDatabase) {
            var idProduct = 1
            val dama = 1
            val caballero = 2
            val products = listOf(
                Triple("Blusa estampada", R.drawable.productm1, dama),
                Triple("Blusa de tirantes", R.drawable.productm2, dama),
                Triple("Crop top con letras", R.drawable.productm3, dama),
                Triple("Vestido largo", R.drawable.productm4, dama),
                Triple("Vestido de fiesta", R.drawable.productm5, dama),
                Triple("Short de mezclilla", R.drawable.productm6, dama),
                Triple("Falda de flores", R.drawable.productm7, dama),
                Triple("Pantalon de mezclilla", R.drawable.productm8, dama),
                Triple("Pantalon formal", R.drawable.productm9, dama),
                Triple("Top con cuello", R.drawable.productm10, dama),

                Triple("Sudadera", R.drawable.producth1, caballero),
                Triple("Pantalon de mezclilla", R.drawable.producth2, caballero),
                Triple("Camisa rayada", R.drawable.producth3, caballero),
                Triple("Pants", R.drawable.producth4, caballero),
                Triple("Bermuda", R.drawable.producth5, caballero),
                Triple("Set pijama", R.drawable.producth6, caballero),
                Triple("Playera animado", R.drawable.producth7, caballero),
                Triple("Jersey tejido", R.drawable.producth8, caballero),
                Triple("Pantalon a medida", R.drawable.producth9, caballero),
                Triple("Camisa vaquera", R.drawable.producth10, caballero)
            )

            val color = "Azul"
            val price = 100f

            for ((name, img, categoryId) in products) {
                val values = ContentValues().apply {
                    put("id", idProduct++)
                    put("name", name)
                    put("category_id", categoryId)
                    put("color", color)
                    put("price", price)
                    put("image", img) // <- CAMBIO aquí
                    put("stock_s", 100)
                    put("stock_m", 50)
                    put("stock_x", 100)
                }
                db.insert("products", null, values)
            }
        }

        private fun setUsers(db: SQLiteDatabase) {
            val users = listOf(
                Triple("tomas11", "tomas@hotmail.com", "123"),
                Triple("didier32", "didier@hotmail.com", "1234"),
                Triple("josearm21", "josearmando@outlook.es", "12345"),
                Triple("maribel07", "maribel@live.com", "123456")
            )
            for ((username, email, pass) in users) {
                val values = ContentValues().apply {
                    put("username", username)
                    put("email", email)
                    put("password", pass)
                }
                db.insert("users", null, values)
            }
        }

        private fun setShoppingCart(db: SQLiteDatabase) {
            val cart = listOf(
                0 to listOf(10, 11, 12, 13),
                1 to listOf(14, 15),
                2 to listOf(16, 17, 18),
                3 to listOf(1, 2, 3)
            )
            cart.forEach { (userId, products) ->
                products.forEach { pid ->
                    val values = ContentValues().apply {
                        put("user_id", userId)
                        put("product_id", pid)
                    }
                    db.insert("cart", null, values)
                }
            }
        }

        private fun setFavorites(db: SQLiteDatabase) {
            val favs = listOf(
                0 to listOf(18, 19),
                1 to listOf(10, 11),
                2 to listOf(12, 14, 15),
                3 to listOf(5, 6, 7, 8)
            )
            favs.forEach { (userId, products) ->
                products.forEach { pid ->
                    val values = ContentValues().apply {
                        put("user_id", userId)
                        put("product_id", pid)
                    }
                    db.insert("favorites", null, values)
                }
            }
        }

        private fun setAddress(db: SQLiteDatabase) {
            val address = mapOf(
                0 to "Francisco Zarco 592, Mexico, Durango, Lerdo, 35150",
                1 to "Miguel Aleman 119, Mexico, Jalisco, Guadalajara, 21170"
            )
            address.forEach { (uid, addr) ->
                val values = ContentValues().apply {
                    put("user_id", uid)
                    put("address", addr)
                }
                db.insert("address", null, values)
            }
        }

        private fun setPaymentMethod(db: SQLiteDatabase) {
            val payment = mapOf(
                0 to Pair("1234567890123456", "1234567890123457"),
                2 to Pair("", "1234567890123458")
            )
            payment.forEach { (uid, cards) ->
                val values = ContentValues().apply {
                    put("user_id", uid)
                    put("debit", cards.first)
                    put("credit", cards.second)
                }
                db.insert("payment", null, values)
            }
        }

        fun authLogin(context: Context, user: String, pass: String): Boolean {
            val db = BaseDatos(context).readableDatabase

            val query = "SELECT * FROM users WHERE username = ? AND password = ?"
            val cursor = db.rawQuery(query, arrayOf(user, pass))

            val isAuthenticated = cursor.count > 0
            cursor.close()
            db.close()
            return isAuthenticated
        }

        fun getProducts(context: Context): List<Product> {
            val db = BaseDatos(context).readableDatabase
            val productList = mutableListOf<Product>()

            val query = """
                SELECT p.*, c.name as category_name 
                FROM products p 
                JOIN categories c ON p.category_id = c.id
            """.trimIndent()

            val cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"))
                    val categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"))
                    val color = cursor.getString(cursor.getColumnIndexOrThrow("color"))
                    val price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"))
                    val image = cursor.getInt(cursor.getColumnIndexOrThrow("image"))
                    val stockS = cursor.getInt(cursor.getColumnIndexOrThrow("stock_s"))
                    val stockM = cursor.getInt(cursor.getColumnIndexOrThrow("stock_m"))
                    val stockX = cursor.getInt(cursor.getColumnIndexOrThrow("stock_x"))

                    val product = Product(
                        idProduct = id,
                        name = name,
                        category = Category(categoryId, categoryName),
                        color = color,
                        price = price,
                        image = image,
                        quantity = mapOf(
                            "S" to stockS,
                            "M" to stockM,
                            "X" to stockX
                        )
                    )

                    productList.add(product)
                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
            return productList
        }
    }
}
