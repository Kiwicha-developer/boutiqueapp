package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.repository.ProductRepository
import com.cibertec.boutiquesmart.repository.UserRepository
import com.cibertec.boutiquesmart.services.RecyclerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchProductActivity : AppCompatActivity(){
    private lateinit var db: DBHelper
    private lateinit var productRepository: ProductRepository
    private lateinit var userRepository: UserRepository
    private lateinit var items_list: RecyclerView
    private lateinit var input_search: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        db = DBHelper(this)
        productRepository = ProductRepository(db)
        userRepository = UserRepository(db)
        items_list = findViewById(R.id.search_datalist)
        input_search = findViewById(R.id.search_search)
        val menu_bar = findViewById<BottomNavigationView>(R.id.search_menu)

        val userId = intent.getIntExtra("id_user",-1)
        val userModel = userRepository.getUserById(userId)

        var products = productRepository.getAllProducts().toMutableList()

        if(userModel != null){
            setAdapter(products,userModel.id)
        }else{
            setAdapter(products,-1)
        }

        input_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                val result = productRepository.searchProductsByName(input_search.text.toString().trim())
                if (result.isNotEmpty()) {
                    products = result.toMutableList()
                    if(userModel != null){
                        setAdapter(products,userModel.id)
                    }else{
                        setAdapter(products,-1)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        // Navegación inferior
        menu_bar.setOnItemSelectedListener {
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

    }

    private fun setAdapter(products: MutableList<Product>, userId: Int) {
        var adapter = RecyclerAdapter(this, products, userId)
        var gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        items_list.setLayoutManager(gridLayoutManager)
        items_list.setAdapter(adapter)
    }
}