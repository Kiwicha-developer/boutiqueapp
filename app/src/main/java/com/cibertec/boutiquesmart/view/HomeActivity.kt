package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.ProductRepository
import com.cibertec.boutiquesmart.repository.UserRepository
import com.cibertec.boutiquesmart.services.ShopContainer
import com.cibertec.boutiquesmart.services.ShopContainerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp

class HomeActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var productsRepository: ProductRepository
    private lateinit var womenViewPager: ViewPager2
    private lateinit var menViewPager: ViewPager2
    private lateinit var kidViewPager: ViewPager2
    private lateinit var inputSearch: Button
    private lateinit var menuBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        db = DBHelper(this)
        productsRepository = ProductRepository(db)
        FirebaseApp.initializeApp(this)

        womenViewPager = findViewById(R.id.WomenSlider)
        menViewPager = findViewById(R.id.MenSlider)
        kidViewPager = findViewById(R.id.KidSlider)
        inputSearch = findViewById(R.id.home_search)
        menuBar = findViewById(R.id.home_menu)

        val userId = intent.getIntExtra("id_user",-1)
        // Buscar productos
        inputSearch.setOnClickListener() {
            val intent = Intent(this, SearchProductActivity::class.java).apply {
                putExtra("id_user", userId)
            }
            startActivity(intent)
        }

        // Navegación inferior
        menuBar.setOnItemSelectedListener {
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

        val products = productsRepository.getAllProducts()

        //slider mujer
        val sliderWoman = (1 .. 5).map {
            ShopContainer(
                image = products[it].image,
                title = products[it].name,
                location = products[it].price
            )
        }

        womenViewPager.adapter = ShopContainerAdapter(sliderWoman.toMutableList())
        configurarSlider(womenViewPager)

        // Slider Hombres
        val sliderMen = (11..15).map {
            ShopContainer(
                image = products[it].image,
                title = products[it].name,
                location = products[it].price
            )
        }
        menViewPager.adapter = ShopContainerAdapter(sliderMen.toMutableList())
        configurarSlider(menViewPager)

        // Slider Niños
        val sliderKid = (20..23).map {
            ShopContainer(
                image = products[it].image,
                title = products[it].name,
                location = products[it].price
            )
        }
        kidViewPager.adapter = ShopContainerAdapter(sliderKid.toMutableList())
        configurarSlider(kidViewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun configurarSlider(viewPager: ViewPager2) {
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.95f + r * 0.05f
        }

        viewPager.setPageTransformer(transformer)
    }
}