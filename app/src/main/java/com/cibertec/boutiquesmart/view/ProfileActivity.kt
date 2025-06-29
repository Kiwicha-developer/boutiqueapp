package com.cibertec.boutiquesmart.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.model.User
import com.cibertec.boutiquesmart.repository.FavoritesRepository
import com.cibertec.boutiquesmart.repository.UserRepository
import com.cibertec.boutiquesmart.services.RecyclerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity: AppCompatActivity(){
    private lateinit var db: DBHelper
    private lateinit var userRepository: UserRepository
    private lateinit var favoritesRepository: FavoritesRepository
    private lateinit var Adapter : RecyclerAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var favoriteIcon: ImageView
    private lateinit var orderIcon: ImageView
    private lateinit var configIcon: ImageButton
    private lateinit var products: MutableList<Product>
    private lateinit var menu_bar : BottomNavigationView
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = DBHelper(this)
        userRepository = UserRepository(db)
        favoritesRepository = FavoritesRepository(db)
        favoriteIcon=findViewById(R.id.profile_img_favorite)
        orderIcon=findViewById(R.id.profile_img_history)
        configIcon=findViewById(R.id.profile_btn_config)
        menu_bar = findViewById(R.id.profile_menu)
        recyclerView=findViewById(R.id.favoriteslist)

        currentUserId = intent.getIntExtra("id_user",-1)

        val user = userRepository.getUserById(currentUserId)

        orderIcon.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java).apply {
                putExtra("id_user", currentUserId)
            }

            startActivity(intent)
        }


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
                    putExtra("id_user", currentUserId)
                }
                startActivity(intent)
            }
            true
        }

        if (user != null) {
            products = favoritesRepository.getFavoritesByUserId(user.id).toMutableList()
        } else {
            products = mutableListOf()
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        Adapter = RecyclerAdapter(this, products, user?.id?: -1)
        recyclerView.adapter = Adapter
    }

    fun showPopup(v: View) {
        val popupMenu = PopupMenu(this, v)

        popupMenu.menuInflater.inflate(R.menu.configuration_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_data -> {
                    val intent = Intent(this, ConfigAccountDataActivity::class.java).apply {
                        putExtra("id_user", currentUserId)
                    }

                    startActivity(intent)
                }
                R.id.menu_address -> {
                    val intent = Intent(this, ConfigAddressActivity::class.java).apply {
                        putExtra("id_user", currentUserId)
                    }

                    startActivity(intent)
                }
                R.id.menu_payment -> {
                    val intent = Intent(this, ConfigPaymentActivity::class.java).apply {
                        putExtra("id_user", currentUserId)
                    }

                    startActivity(intent)
                }
                R.id.menu_logout -> {
                    confirmDialog()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun confirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Cerrar sesión")
        builder.setMessage("¿Estas segur@ que quieres cerrar sesión?")

        builder.setNegativeButton(
            "No"
        ) { dialog, which -> }

        builder.setPositiveButton(
            "Si"
        ) { dialogInterface, i ->
            Firebase.auth.signOut()
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            dialogInterface.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}