package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.repository.UserRepository

class AddCardActivity : AppCompatActivity() {

    private lateinit var cardNumber: EditText
    private lateinit var cardExpiry: EditText
    private lateinit var cardCVV: EditText
    private lateinit var addCardButton: Button

    private lateinit var userRepository: UserRepository
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        userId = intent.getIntExtra("id_user", -1)

        userRepository = UserRepository(DBHelper(this))

        cardNumber = findViewById(R.id.add_card_input_number)
        cardExpiry = findViewById(R.id.add_card_input_expiry)
        cardCVV = findViewById(R.id.add_card_input_cvv)
        addCardButton = findViewById(R.id.add_card_btn_add)

        addCardButton.setOnClickListener {
            val number = cardNumber.text.toString().trim()
            val expiry = cardExpiry.text.toString().trim()
            val cvv = cardCVV.text.toString().trim()

            when {
                !isValidCard(number) -> showToast("Número inválido (debe comenzar con 3, 4 o 5 y tener 16 dígitos)")
                !isValidExpiry(expiry) -> showToast("Vencimiento inválido (MM/AA)")
                !isValidCVV(cvv) -> showToast("CVV inválido (3 dígitos)")
                else -> {
                    val user = userRepository.getUserById(userId)
                    if (user != null) {
                        user.cardNumber = number
                        user.cardVencimiento = expiry
                        user.cardCVV = cvv
                        userRepository.updateUser(user)
                        Toast.makeText(this, "Tarjeta agregada", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ConfigPaymentActivity::class.java)
                        intent.putExtra("id_user", userId)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("Usuario no encontrado")
                    }
                }
            }
        }
    }

    private fun isValidCard(card: String): Boolean {
        return card.length == 16 && (card.startsWith("3") || card.startsWith("4") || card.startsWith("5"))
    }

    private fun isValidExpiry(expiry: String): Boolean {
        return expiry.matches(Regex("""(0[1-9]|1[0-2])\/\d{2}"""))
    }

    private fun isValidCVV(cvv: String): Boolean {
        return cvv.length == 3 && cvv.all { it.isDigit() }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
