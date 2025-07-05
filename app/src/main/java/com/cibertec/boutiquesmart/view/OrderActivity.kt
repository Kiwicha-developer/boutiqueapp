package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibertec.boutiquesmart.databinding.ActivityOrdersBinding
import com.cibertec.boutiquesmart.model.Pedido
import com.cibertec.boutiquesmart.repository.UserRepository
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.services.PedidoAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var pedidosList: MutableList<Pedido>
    private lateinit var adapter: PedidoAdapter
    private lateinit var userRepository: UserRepository
    private lateinit var sql: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sql = DBHelper(this)
        userRepository = UserRepository(sql)
        db = Firebase.firestore

        val userId = intent.getIntExtra("id_user", -1)
        val user = userRepository.getUserById(userId)

        if (user == null) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        pedidosList = mutableListOf()
        adapter = PedidoAdapter(pedidosList)

        binding.orderlist.layoutManager = LinearLayoutManager(this)
        binding.orderlist.adapter = adapter

        traerPedidosPorCorreo(user.email)

        binding.orderImgFavorite.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra("id_user", userId)
            startActivity(intent)
        }
    }

    private fun traerPedidosPorCorreo(correo: String) {
        db.collection("pedidos")
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener { documents ->
                pedidosList.clear()
                for (doc in documents) {
                    val pedido = Pedido(
                        producto = doc["producto"].toString(),
                        cantidad = doc["cantidad"].toString(),
                        precioUnitario = doc["precio_unitario"].toString(),
                        estado = doc["estado"].toString(),
                        fecha = doc["fecha"].toString()
                    )
                    pedidosList.add(pedido)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al traer pedidos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
