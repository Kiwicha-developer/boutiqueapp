package com.cibertec.boutiquesmart.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.databinding.ActivityConfigAddressBinding

class ConfigAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigAddressBinding
    private var userId: Int = -1

    companion object {
        const val REQUEST_CODE_ADD_ADDRESS = 100
        const val EXTRA_ADDRESS = "direccion_guardada"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar ID del usuario desde el intent
        userId = intent.getIntExtra("id_user", -1)

        // Cargar dirección guardada
        val prefs = getSharedPreferences("direccion_prefs", MODE_PRIVATE)
        val direccionGuardada = prefs.getString("direccion_guardada", "")
        binding.configAddressInputAddress.setText(direccionGuardada)
        // Botón para abrir EditAddressActivity
        binding.configAddressBtnAddNew.setOnClickListener {
            val intent = Intent(this, EditAddressActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
        }

        // Botón "Volver" para regresar a ProfileActivity
        binding.configAddressBtnReturn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_ADDRESS && resultCode == Activity.RESULT_OK) {
            val direccion = data?.getStringExtra(EXTRA_ADDRESS)
            binding.configAddressInputAddress.setText(direccion)
        }
    }
}

