package com.cibertec.boutiquesmart.view

import android.animation.AnimatorInflater
import android.app.ActivityOptions
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var inp_username: EditText
    private lateinit var inp_password: EditText
    private lateinit var btn_login: Button
    private lateinit var btn_signin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        db = DBHelper(this)
        userRepository = UserRepository(db)
        inp_username = findViewById(R.id.start_input_username)
        inp_password = findViewById(R.id.start_input_password)
        btn_login = findViewById(R.id.start_btn_login)
        btn_signin = findViewById(R.id.start_btn_signin)


        btn_login.setOnClickListener {
            if (inp_username.text != null && inp_username.text != null) {
                iniciarSesion(inp_username, inp_password)
            } else {
                Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show()
            }
        }

        btn_signin.setOnClickListener {
            if (inp_username.text != null && inp_username.text != null) {
                registrarse(inp_username, inp_password)
            } else {
                Toast.makeText(this, "Campos vacios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun iniciarSesion(username: EditText, password: EditText) {
        val user = username.text.toString().trim()
        val pass = password.text.toString().trim()

        val userModel = userRepository.login(user, pass)

        if (userModel != null) {
            Toast.makeText(this, "INICIO DE SESIÓN EXITOSO", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("id_user",userModel.id)
            startActivity(intent)
        } else {
            Toast.makeText(this, "ERROR AL INICIAR SESIÓN", Toast.LENGTH_SHORT).show()
        }
    }

    fun registrarse(username: EditText, password: EditText) {
        val user = username.text.toString().trim()
        val pass = password.text.toString().trim()

        val userModel = User(
            id = -1,
            username = user,
            password = pass,
            email = "${user}@boutique.com",
            nombre= "",
            documento = "",
            address = "",
            cardNumber = "",
            cardVencimiento = "",
            cardCVV = ""
        )

        val result = userRepository.insertUser(userModel)
        if (result != -1L) {
            Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al insertar usuario", Toast.LENGTH_SHORT).show()
        }
    }

}