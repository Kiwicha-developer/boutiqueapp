package com.cibertec.boutiquesmart.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.UserRepository

class ConfigAccountDataActivity : AppCompatActivity(){
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var returnIcon: Button
    private lateinit var editUsernameIcon: Button
    private lateinit var editEmailIcon: Button
    private lateinit var editPasswordIcon: Button
    private lateinit var inputUsername: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_account_data)

        db = DBHelper(this)
        userRepository = UserRepository(db)
        returnIcon = findViewById(R.id.config_data_btn_return)
        editUsernameIcon = findViewById(R.id.config_data_btn_edit_username)
        editEmailIcon = findViewById(R.id.config_data_btn_edit_email)
        editPasswordIcon = findViewById(R.id.config_data_btn_edit_password)
        inputUsername = findViewById(R.id.config_data_input_username)
        inputEmail = findViewById(R.id.config_data_input_email)
        inputPassword = findViewById(R.id.config_data_input_password)
        updateButton = findViewById(R.id.config_data_btn_update)

        val userId = intent.getIntExtra("id_user",-1)
        val user = userRepository.getUserById(userId)

        returnIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id_user",userId)
            startActivity(intent)
        }

        if(user != null){
            // Mostrar datos actuales
            inputUsername.setText(user.username)
            inputEmail.setText(user.email)

            // Activar campos al presionar íconos
            editUsernameIcon.setOnClickListener { inputUsername.isEnabled = true }
            editEmailIcon.setOnClickListener { inputEmail.isEnabled = true }
            editPasswordIcon.setOnClickListener { inputPassword.isEnabled = true }

            // Detectar cambios
            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateButton.isVisible = true
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            inputUsername.addTextChangedListener(watcher)
            inputEmail.addTextChangedListener(watcher)
            inputPassword.addTextChangedListener(watcher)

            // Botón actualizar
            updateButton.setOnClickListener {
                showConfirmDialog(user,userRepository)
            }

        } else {
            Toast.makeText(this, "Inicia sesión o crea una cuenta :) ${userId}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConfirmDialog(user: User, userRepository: UserRepository) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Actualización de datos")
        builder.setMessage("¿Deseas cambiar tus datos?")

        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        builder.setPositiveButton("Sí") { dialog, _ ->
            val newName = inputUsername.text.toString().trim()
            val newEmail = inputEmail.text.toString().trim()
            val newPassword = inputPassword.text.toString().trim()

            if (newName.isNotBlank() && newEmail.isNotBlank() && isEmailValid(newEmail) && newPassword.isNotBlank() && isPasswordValid(newPassword)) {
                user.username = newName
                user.email = newEmail
                user.password = newPassword

                userRepository.updateUser(user)
            }

            Toast.makeText(this, "Tus datos han sido actualizados", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
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