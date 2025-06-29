package com.cibertec.boutiquesmart.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.repository.ProductRepository
import com.cibertec.boutiquesmart.services.DetailFragment

class DetailActivity: AppCompatActivity() {
    private lateinit var db:DBHelper
    private lateinit var productRepository: ProductRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        db = DBHelper(this)
        productRepository = ProductRepository(db)

        val idProduct = intent.getIntExtra("id_product",-1)
        val product = productRepository.getProductById(idProduct)

        if (product == null) {
            Toast.makeText(this, "Producto no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentDetail)
        if (fragment is DetailFragment) {
            fragment.showProduct(product)
        } else {
            Toast.makeText(this, "Fragmento no disponible", Toast.LENGTH_SHORT).show()
        }
    }
}