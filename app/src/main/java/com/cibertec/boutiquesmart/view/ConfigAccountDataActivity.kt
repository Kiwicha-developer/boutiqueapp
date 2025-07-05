package com.cibertec.boutiquesmart.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.UserRepository

class ConfigAccountDataActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var returnIcon: Button
    private lateinit var inputUsername: EditText
    private lateinit var inputName: EditText
    private lateinit var inputLastname: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_account_data)

        db = DBHelper(this)
        userRepository = UserRepository(db)

        returnIcon = findViewById(R.id.config_data_btn_return)
        inputUsername = findViewById(R.id.config_data_input_username)
        inputName = findViewById(R.id.config_data_input_name)
        inputLastname = findViewById(R.id.config_data_input_lastname)
        inputEmail = findViewById(R.id.config_data_input_email)
        inputPassword = findViewById(R.id.config_data_input_password)
        updateButton = findViewById(R.id.config_data_btn_update)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val savedUsername = prefs.getString("username", "")
        val savedEmail = prefs.getString("email", "")

        inputUsername.setText(savedUsername)
        inputEmail.setText(savedEmail)


        val userId = intent.getIntExtra("id_user", -1)
        val user = userRepository.getUserById(userId)

        returnIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
        }

        if (user != null) {
            inputUsername.setText(user.username)
            inputName.setText(user.nombre)
            inputLastname.setText(user.apellido)
            inputEmail.setText(user.email)

            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateButton.isVisible = true
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            inputUsername.addTextChangedListener(watcher)
            inputName.addTextChangedListener(watcher)
            inputLastname.addTextChangedListener(watcher)
            inputEmail.addTextChangedListener(watcher)
            inputPassword.addTextChangedListener(watcher)

            updateButton.setOnClickListener {
                showConfirmDialog(user)
            }
        } else {
            Toast.makeText(this, "Inicia sesión o crea una cuenta :) $userId", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConfirmDialog(user: User) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Actualización de datos")
        builder.setMessage("¿Deseas cambiar tus datos?")

        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        builder.setPositiveButton("Sí") { dialog, _ ->
            val newUsername = inputUsername.text.toString().trim()
            val newName = inputName.text.toString().trim()
            val newLast = inputLastname.text.toString().trim()
            val newEmail = inputEmail.text.toString().trim()
            val newPass = inputPassword.text.toString().trim()

            when {
                newUsername.isBlank() -> {
                    Toast.makeText(this, "El nombre de usuario es obligatorio", Toast.LENGTH_SHORT).show()
                }
                newName.isBlank() -> {
                    Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                }
                newLast.isBlank() -> {
                    Toast.makeText(this, "El apellido es obligatorio", Toast.LENGTH_SHORT).show()
                }
                newEmail.isBlank() -> {
                    Toast.makeText(this, "El correo electrónico es obligatorio", Toast.LENGTH_SHORT).show()
                }
                !isEmailValid(newEmail) -> {
                    Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show()
                }
                newPass.isBlank() -> {
                    Toast.makeText(this, "La contraseña es obligatoria", Toast.LENGTH_SHORT).show()
                }
                !isPasswordValid(newPass) -> {
                    Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    user.username = newUsername
                    user.nombre = newName
                    user.apellido = newLast
                    user.email = newEmail
                    user.password = newPass

                    userRepository.updateUser(user)
                    Toast.makeText(this, "Tus datos han sido actualizados", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }

        builder.create().show()
    }


    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}
