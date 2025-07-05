package com.cibertec.boutiquesmart.services

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R

class CongratsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)

        val uri = intent?.data
        val status = uri?.getQueryParameter("status")
        val paymentId = uri?.getQueryParameter("payment_id")

        if (status == "approved") {
            Toast.makeText(this, "✅ Pago aprobado (ID: $paymentId)", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "⚠️ Pago no aprobado: $status", Toast.LENGTH_LONG).show()
        }
    }
}