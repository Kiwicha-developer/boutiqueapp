package com.cibertec.boutiquesmart.repository

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cibertec.boutiquesmart.model.Category

class CategoryRepository(private val dbHelper: SQLiteOpenHelper) {
    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM categories", null)

        if (cursor.moveToFirst()) {
            do {
                val category = Category(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                )
                categories.add(category)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return categories
    }
}