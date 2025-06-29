package com.cibertec.boutiquesmart.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.UserRepository

class ConfigPaymentActivity: AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var returnIcon: Button
    private lateinit var editDebitCard: Button
    private lateinit var editCreditCard: Button
    private lateinit var inputDebitCard: EditText
    private lateinit var inputCreditCard: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_payment)

        db = DBHelper(this)
        userRepository = UserRepository(db)
        returnIcon = findViewById(R.id.config_pay_btn_return)
        editDebitCard = findViewById(R.id.config_pay_btn_edit_debit)
        editCreditCard = findViewById(R.id.config_pay_btn_edit_credit)
        inputDebitCard = findViewById(R.id.config_pay_input_debit)
        inputCreditCard = findViewById(R.id.config_pay_input_credit)
        updateButton = findViewById(R.id.config_pay_btn_update)

        val userId = intent.getIntExtra("id_user",-1)
        val user = userRepository.getUserById(userId)

        returnIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id_user",userId)
            startActivity(intent)
        }

        if (user != null) {
            inputCreditCard.setText(user.payment)
            inputDebitCard.setText(user.typePayment)

            editDebitCard.setOnClickListener { inputDebitCard.isEnabled = true }
            editCreditCard.setOnClickListener { inputCreditCard.isEnabled = true }

            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateButton.isVisible = true
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            inputDebitCard.addTextChangedListener(watcher)
            inputCreditCard.addTextChangedListener(watcher)

            updateButton.setOnClickListener {
                showConfirmationDialog(user,userRepository)
            }
        } else {
            Toast.makeText(this, "Inicia sesión o crea una cuenta :)", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConfirmationDialog(user: User,userRepository: UserRepository) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Actualización de datos")
            .setMessage("¿Deseas cambiar tus datos de pago?")
            .setCancelable(true)
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Sí") { dialog, _ ->
                val creditCard = inputCreditCard.text.toString().trim()
                val debitCard = inputDebitCard.text.toString().trim()

                user.payment = creditCard
                user.typePayment = debitCard

                if (isCardValid(creditCard) && isCardValid(debitCard)) {
                    userRepository.updateUser(user)
                }

                Toast.makeText(this, "Tus datos han sido actualizados", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun isCardValid(card: String): Boolean {
        return card.length in 13..19 && card.all { it.isDigit() }
    }

}