package com.cibertec.boutiquesmart.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.Menu
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.cibertec.boutiquesmart.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_CLIENT = "CHANNEL_COURSES"
    }

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_shop)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setNotificationChannel()
//        }
//
//        touchNotification()

        val menuBar = findViewById<BottomNavigationView>(R.id.home_menu)
        val inputSearch = findViewById<Button>(R.id.home_search)
        val username = "tomas11"

        // Animación al entrar
        val transition = Slide(Gravity.TOP).apply {
            duration = 500
            //excludeTarget(window.decorView.findViewById<View>(R.id.action_bar_container), true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
        }
        window.enterTransition = transition

//        // Navegación inferior
//        menuBar.setOnItemSelectedListener {
//            val bundle = Bundle().apply { putString(USERNAME, username) }
//            val target = when (it.itemId) {
//                R.id.menu_shopping_cart -> CartActivity::class.java
//                R.id.menu_community -> CommunityActivity::class.java
//                R.id.menu_profile -> ProfileActivity::class.java
//                else -> null
//            }
//            target?.let { activity ->
//                startActivity(Intent(this, activity).apply { putExtras(bundle) })
//            }
//            true
//        }
//
//        // Buscar productos
//        inputSearch.setOnClickListener() {
//            val intent = Intent(this, SearchProductActivity::class.java).apply {
//                putExtra(USERNAME, username)
//            }
//            startActivity(intent)
//        }
//
//        val products = MYSTORE.catalogProduct
//
//        // Slider Mujeres
//        val womenViewPager = findViewById<ViewPager2>(R.id.WomenSlider)
//        val sliderWomen = (1..5).map {
//            ShopContainer(
//                image = products[it].image,
//                title = products[it].name,
//                location = products[it].price
//            )
//        }
//        womenViewPager.adapter = ShopContainerAdapter(sliderWomen.toMutableList())
//        configurarSlider(womenViewPager)
//
//        // Slider Hombres
//        val menViewPager = findViewById<ViewPager2>(R.id.MenSlider)
//        val sliderMen = (11..15).map {
//            ShopContainer(
//                image = products[it].image,
//                title = products[it].name,
//                location = products[it].price
//            )
//        }
//        menViewPager.adapter = ShopContainerAdapter(sliderMen.toMutableList())
//        configurarSlider(menViewPager)
//
//        // Slider Niños
//        val kidViewPager = findViewById<ViewPager2>(R.id.KidSlider)
//        val sliderKid = mutableListOf(
//            ShopContainer(R.drawable.image_n1, "Chamarra negra", 400.0f),
//            ShopContainer(R.drawable.image_n2, "Gorra naranja", 100.0f),
//            ShopContainer(R.drawable.image_n3, "Chaleco azul", 200.0f),
//            ShopContainer(R.drawable.image_n4, "Chamarra gris", 300.0f)
//        )
//        kidViewPager.adapter = ShopContainerAdapter(sliderKid)
//        configurarSlider(kidViewPager)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.navigation_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    private fun configurarSlider(viewPager: ViewPager2) {
//        viewPager.clipToPadding = false
//        viewPager.clipChildren = false
//        viewPager.offscreenPageLimit = 3
//        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//
//        val transformer = CompositePageTransformer()
//        transformer.addTransformer(MarginPageTransformer(40))
//        transformer.addTransformer { page, position ->
//            val r = 1 - kotlin.math.abs(position)
//            page.scaleY = 0.95f + r * 0.05f
//        }
//
//        viewPager.setPageTransformer(transformer)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun setNotificationChannel() {
//        val name = getString(R.string.channel_client)
//        val descriptionText = getString(R.string.transaction_description)
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//
//        val channel = NotificationChannel(CHANNEL_CLIENT, name, importance).apply {
//            description = descriptionText
//        }
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    private fun touchNotification() {
//        val intent = Intent(this, CommunityActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val notification = NotificationCompat.Builder(this, CHANNEL_CLIENT)
//            .setSmallIcon(R.drawable.ic_logo)
//            .setColor(ContextCompat.getColor(this, R.color.blue))
//            .setLargeIcon(getDrawable(R.drawable.producth10)?.toBitmap())
//            .setContentTitle(getString(R.string.action_title_welcome))
//            .setContentText(getString(R.string.action_body_welcome))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .build()
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        NotificationManagerCompat.from(this).notify(24, notification)
    }
}