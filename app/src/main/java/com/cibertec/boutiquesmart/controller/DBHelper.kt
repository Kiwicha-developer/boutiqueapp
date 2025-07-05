package com.cibertec.boutiquesmart.controller

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context,"boutique.db",null,2) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
        CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                nombre TEXT,
                documento TEXT,
                email TEXT,
                password TEXT,
                address TEXT,
                cardNumber TEXT,
                cardVencimiento TEXT,
                cardCVV TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE categories (
                id INTEGER PRIMARY KEY,
                name TEXT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE products (
                id INTEGER PRIMARY KEY,
                name TEXT,
                category_id INTEGER,
                color TEXT,
                price REAL,
                image TEXT,
                stock_s INTEGER,
                stock_m INTEGER,
                stock_x INTEGER,
                FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE cart (
                user_id INTEGER,
                product_id INTEGER,
                cantidad INTEGER DEFAULT 1,
                FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
                UNIQUE(user_id, product_id)
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE favorites (
                user_id INTEGER,
                product_id INTEGER,
                FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
            )
        """.trimIndent())

        // Insertar categorías por defecto
        db.execSQL("INSERT INTO categories(id, name) VALUES (1, 'Dama')")
        db.execSQL("INSERT INTO categories(id, name) VALUES (2, 'Caballero')")
        db.execSQL("INSERT INTO categories(id, name) VALUES (3, 'Niño')")

        // Productos Dama (category_id = 1)
        db.execSQL("INSERT INTO products VALUES (1,'Blusa estampada',1,'Blanca',0.10,'productm1',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (2,'Blusa de tirantes',1,'Blanco',300,'productm2',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (3,'Crop top con letras',1,'Gris',350,'productm3',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (4,'Vestido largo',1,'Verde',900,'productm4',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (5,'Vestido de fiesta',1,'Aqua',400,'productm5',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (6,'Short de mezclilla',1,'Azul claro',150,'productm6',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (7,'Falda de flores',1,'Rosa',200,'productm7',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (8,'Pantalon de mezclilla',1,'Azul',350,'productm8',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (9,'Pantalon formal',1,'Azul',400,'productm9',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (10,'Top con cuello',1,'Lila',150,'productm10',100,50,100)")

        // Productos Caballero (category_id = 2)
        db.execSQL("INSERT INTO products VALUES (11,'Sudadera',2,'Lila',250,'producth1',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (12,'Pantalon de mezclilla',2,'Azul',400,'producth2',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (13,'Camisa rayada',2,'Blanco',80,'producth3',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (14,'Pants',2,'Gris',120,'producth4',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (15,'Bermuda',2,'Caqui',250,'producth5',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (16,'Set pijama',2,'Gris/azul',250,'producth6',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (17,'Playera animado',2,'Beige',150,'producth7',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (18,'Jersey tejido',2,'Gris',180,'producth8',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (19,'Pantalon a medida',2,'Azul',320,'producth9',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (20,'Camisa vaquera',2,'Azul',180,'producth10',100,50,100)")


        // Productos Niño (category_id = 3)
        db.execSQL("INSERT INTO products VALUES (21,'Chamarra amarilla',3,'Amarillo',120,'image_n1',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (22,'Camiseta azul',3,'Azul',90,'image_n2',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (23,'Gorra Azul',3,'Azul',140,'image_n3',100,50,100)")
        db.execSQL("INSERT INTO products VALUES (24,'Polo Camisero Rojo',3,'Rojo',100,'image_n4',100,50,100)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS products")
        db.execSQL("DROP TABLE IF EXISTS cart")
        db.execSQL("DROP TABLE IF EXISTS favorites")
        onCreate(db)
    }
}