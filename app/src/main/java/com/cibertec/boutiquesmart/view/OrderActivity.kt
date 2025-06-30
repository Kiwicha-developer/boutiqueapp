package com.cibertec.boutiquesmart.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibertec.boutiquesmart.services.CartAdapter
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.databinding.ActivityOrdersBinding
import com.cibertec.boutiquesmart.repository.CartRepository


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private lateinit var cartRepository: CartRepository
    private var userId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartRepository = CartRepository(DBHelper(this))

        val cartItems = cartRepository.getCartByUserId(userId)

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            finish()
        }

        val adapter = CartAdapter(cartItems)
        binding.orderlist.layoutManager = LinearLayoutManager(this)
        binding.orderlist.adapter = adapter


        val total = cartItems.sumOf { it.price.toDouble() }
        binding.txtTotal.text = "Total: S/$total"

        binding.btnConfirm.setOnClickListener {

            cartItems.forEach {
                cartRepository.removeFromCart(userId, it.id)
            }
            Toast.makeText(this, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
