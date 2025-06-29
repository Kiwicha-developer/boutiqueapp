package com.cibertec.boutiquesmart.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.UserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class ConfigAddressActivity: AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var returnIcon: Button
    private lateinit var editAddress: Button
    private lateinit var obtenerDireccion: Button
    private lateinit var inputAddress: EditText
    private lateinit var updateButton: Button
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID = 33

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_address)

        // Inicializar variables
        db = DBHelper(this)
        userRepository = UserRepository(db)
        returnIcon = findViewById(R.id.config_address_btn_return)
        editAddress = findViewById(R.id.config_address_btn_edit_address)
        inputAddress = findViewById(R.id.config_address_input_address)
        obtenerDireccion = findViewById(R.id.config_adress_btn_obtener)
        updateButton = findViewById(R.id.config_adress_btn_update)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val userId = intent.getIntExtra("id_user",-1)
        val user = userRepository.getUserById(userId)

        returnIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("id_user",userId)
            startActivity(intent)
        }

        obtenerDireccion.setOnClickListener {
            getLocation()
            inputAddress.isEnabled = true
            updateButton.isVisible = true
        }

        if (user != null) {
            inputAddress.setText(user.address)
            editAddress.setOnClickListener { inputAddress.isEnabled = true }

            inputAddress.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateButton.isVisible = true
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            updateButton.setOnClickListener {
                showConfirmationDialog(user,userRepository)
            }
        } else {
            Toast.makeText(this, "Inicia sesión o crea una cuenta :)", Toast.LENGTH_LONG).show()
        }
    }

    private fun showConfirmationDialog(user: User,userRepository: UserRepository) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Actualización de datos")
        builder.setMessage("¿Deseas cambiar tu dirección?")

        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        builder.setPositiveButton("Sí") { dialog, _ ->
            val newAddress = inputAddress.text.toString()
            if (newAddress.isNotBlank()) {
                user.address = newAddress
                userRepository.updateUser(user)
                Toast.makeText(this, "Dirección actualizada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "La dirección no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.create().show()
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (!addresses.isNullOrEmpty()) {
                                inputAddress.setText(addresses[0].getAddressLine(0))
                            } else {
                                Toast.makeText(this, "No se encontró dirección", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this, "Error al obtener dirección", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No se encontró ubicación actual", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Activa la ubicación", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun checkGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermissions(): Boolean {
        return checkGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}