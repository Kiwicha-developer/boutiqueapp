package com.cibertec.boutiquesmart.repository

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cibertec.boutiquesmart.model.User

class UserRepository(private val dbHelper : SQLiteOpenHelper) {
    fun insertUser(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("nombre", user.nombre)
            put("documento", user.documento)
            put("email", user.email)
            put("password", user.password)
            put("address", user.address)
            put("cardNumber", user.cardNumber)
            put("cardVencimiento", user.cardVencimiento)
            put("cardCVV", user.cardCVV)
        }
        return db.insert("users", null, values)
    }

    fun updateUser(user: User): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("username", user.username)
            put("nombre", user.nombre)
            put("documento", user.documento)
            put("email", user.email)
            put("password", user.password)
            put("address", user.address)
            put("cardNumber", user.cardNumber)
            put("cardVencimiento", user.cardVencimiento)
            put("cardCVV", user.cardCVV)
        }
        return db.update("users", values, "id = ?", arrayOf(user.id.toString()))
    }

    fun getUserById(id: Int): User? {
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                documento = cursor.getString(cursor.getColumnIndexOrThrow("documento")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cardNumber = cursor.getString(cursor.getColumnIndexOrThrow("cardNumber")),
                cardVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("cardVencimiento")),
                cardCVV = cursor.getString(cursor.getColumnIndexOrThrow("cardCVV"))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun login(username: String, password: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                documento = cursor.getString(cursor.getColumnIndexOrThrow("documento")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cardNumber = cursor.getString(cursor.getColumnIndexOrThrow("cardNumber")),
                cardVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("cardVencimiento")),
                cardCVV = cursor.getString(cursor.getColumnIndexOrThrow("cardCVV"))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun listUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    documento = cursor.getString(cursor.getColumnIndexOrThrow("documento")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                    address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cardNumber = cursor.getString(cursor.getColumnIndexOrThrow("cardNumber")),
                    cardVencimiento = cursor.getString(cursor.getColumnIndexOrThrow("cardVencimiento")),
                    cardCVV = cursor.getString(cursor.getColumnIndexOrThrow("cardCVV"))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }
}