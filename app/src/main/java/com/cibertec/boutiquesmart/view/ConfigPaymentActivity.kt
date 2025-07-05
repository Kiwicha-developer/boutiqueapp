package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.UserRepository

class ConfigPaymentActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var inputCard: EditText
    private lateinit var cardDisplay: TextView
    private lateinit var addCardButton: Button
    private lateinit var returnButton: Button

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_payment)

        // Obtener ID de usuario desde el intent
        userId = intent.getIntExtra("id_user", -1)

        // Inicialización de la BD y repositorio
        db = DBHelper(this)
        userRepository = UserRepository(db)

        // Vinculación con elementos del layout
        inputCard = findViewById(R.id.inputCardNumber)
        cardDisplay = findViewById(R.id.cardNumberDisplay)
        addCardButton = findViewById(R.id.btn_add_new_card)
        returnButton = findViewById(R.id.config_pay_btn_return)

        // Mostrar número de tarjeta en formato seguro
        inputCard.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val digits = s.toString().filter { it.isDigit() }
                cardDisplay.text = digits.chunked(4).joinToString(" ").padEnd(19, '•')
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Acción al presionar botón "Agregar tarjeta"
        addCardButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
        }

        // Acción al presionar botón "Volver"
        returnButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
            finish()
        }

        // Cargar tarjeta si existe
        val user = userRepository.getUserById(userId)
        if (user != null && user.payment.isNotEmpty()) {
            // Si ya tiene una tarjeta guardada, la mostramos
            inputCard.setText(user.payment)
        }
    }
}
