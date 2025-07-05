package com.cibertec.boutiquesmart.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.databinding.ActivityCartBinding
import com.cibertec.boutiquesmart.model.Cart
import com.cibertec.boutiquesmart.repository.CartRepository
import com.cibertec.boutiquesmart.repository.UserRepository
import com.cibertec.boutiquesmart.services.DetailFragment
import com.cibertec.boutiquesmart.services.ListFragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.mercadopago.android.px.core.MercadoPagoCheckout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CartActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var cartRepository: CartRepository
    private lateinit var userRepository: UserRepository
    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        cartRepository = CartRepository(db)
        userRepository = UserRepository(db)

        val userId = intent.getIntExtra("id_user",-1)
        val user = userRepository.getUserById(userId)
        val cartUser = cartRepository.getCartByUserId(userId)

        if (user == null) {
            finish()
            return
        }

        binding.cartMenu.setOnItemSelectedListener {
            val target = when(it.itemId) {
                R.id.menu_home -> HomeActivity::class.java
                R.id.menu_shopping_cart -> CartActivity::class.java
                R.id.menu_community -> ComunityActivity::class.java
                R.id.menu_profile -> ProfileActivity::class.java
                else -> null
            }
            target?.let { activity ->
                val intent = Intent(this,activity).apply {
                    putExtra("id_user", userId)
                }
                startActivity(intent)
            }
            true
        }

        val listFragment = ListFragment()
        listFragment.user = user
        listFragment.setListener { product ->
            val detailFragment = supportFragmentManager.findFragmentById(R.id.fragmentDetail) as? DetailFragment
            if (detailFragment != null) {
                detailFragment.showProduct(product)
            } else {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("id_product", product.id)
                startActivity(intent)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.cart_fragment_container, listFragment)
            .commit()


            binding.cartMenu.setOnItemSelectedListener {
                val target = when(it.itemId) {
                    R.id.menu_home -> HomeActivity::class.java
                    R.id.menu_shopping_cart -> CartActivity::class.java
                    R.id.menu_community -> ComunityActivity::class.java
                    R.id.menu_profile -> ProfileActivity::class.java
                    else -> null
                }
                target?.let { activity ->
                    val intent = Intent(this,activity).apply {
                        putExtra("id_user", userId)
                    }
                    startActivity(intent)
                }
                true
            }


        var totalPrice = 0f
        cartUser.forEach { cart ->
            totalPrice += cart.product.price * cart.cantidad
        }

        binding.cartTxtTotal.text = "S/ %.2f".format(totalPrice ?: 0.00)

        binding.cartBtnShop.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val url = getInitPoint(cartUser)
                    val intent = CustomTabsIntent.Builder()
                        .build()
                    intent.launchUrl(this@CartActivity, Uri.parse(url))
                    // Vaciar carrito después de redirección
                    insertRegistro(cartUser)
                    cartUser.forEach {
                        cartRepository.removeFromCart(userId, it.product.id)
                    }
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@CartActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    suspend fun getInitPoint(cart: MutableList<Cart>): String {
        return withContext(Dispatchers.IO) {
            val itemsArray = JSONArray()

            cart.forEach { c ->
                val json = JSONObject().apply {
                    put("title", c.product.name)
                    put("quantity", c.cantidad)
                    put("unit_price", c.product.price)
                }
                itemsArray.put(json)
            }

            val jsonRequest = JSONObject().apply {
                put("items", itemsArray)
            }

            val client = OkHttpClient()

            val requestBody = jsonRequest.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url("http://192.168.1.39:3000/crear-preferencia")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val body = response.body?.string() ?: throw IOException("Empty response body")
            val json = JSONObject(body)
            json.getString("init_point")  // 🔥 Devuelve el `init_point`
        }
    }


    fun actualizarTotal(carts: List<Cart>) {
        var totalPrice = 0f
        carts.forEach { cart ->
            totalPrice += cart.product.price * cart.cantidad
        }
        findViewById<TextView>(R.id.cart_txt_total).text = "S/ %.2f".format(totalPrice)
    }


    private fun insertRegistro(cart: MutableList<Cart>){
        val db = Firebase.firestore

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        cart.forEach { c ->
            val pedido = hashMapOf(
                "cantidad" to c.cantidad.toString(),
                "correo" to c.user.email,
                "estado" to "en proceso",
                "fecha" to fechaActual,
                "precio_unitario" to c.product.price.toString(),
                "producto" to c.product.name,
                "direccion" to c.user.address
            )

            db.collection("pedidos")
                .add(pedido)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "Pedido agregado con ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al agregar documento", e)
                }
        }
    }

}

