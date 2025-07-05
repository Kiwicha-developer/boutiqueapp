package com.cibertec.boutiquesmart.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.databinding.ActivityEditAddressBinding
import com.cibertec.boutiquesmart.view.ConfigAddressActivity.Companion.EXTRA_ADDRESS

class EditAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editAddressBtnSave.setOnClickListener {
            val nombre = binding.editAddressNombre.text.toString()
            val depto = binding.editAddressDepartamento.text.toString()
            val prov = binding.editAddressProvincia.text.toString()
            val dist = binding.editAddressDistrito.text.toString()
            val calle = binding.editAddressCalle.text.toString()
            val telefono = binding.editAddressPhone.text.toString()

            if (nombre.isEmpty() || depto.isEmpty() || prov.isEmpty() || dist.isEmpty() || calle.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val direccionCompleta = """
            $nombre
            $calle
            $dist
            $prov
            $depto
            Tel: $telefono
            """.trimIndent()

            // Guardar en SharedPreferences
            val prefs = getSharedPreferences("direccion_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("direccion_guardada", direccionCompleta).apply()


            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_ADDRESS, direccionCompleta)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
