package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.databinding.ActivityCartBinding
import com.cibertec.boutiquesmart.repository.CartRepository
import com.cibertec.boutiquesmart.repository.UserRepository
import com.cibertec.boutiquesmart.services.DetailFragment
import com.cibertec.boutiquesmart.services.ListFragment

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
        cartUser.forEach { product ->
            totalPrice += product.price
        }

        binding.cartTxtTotal.text = "${totalPrice ?: 0.00}"
    }

}
