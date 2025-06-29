package com.cibertec.boutiquesmart.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.repository.CartRepository
import com.cibertec.boutiquesmart.repository.FavoritesRepository
import com.cibertec.boutiquesmart.repository.ProductRepository
import org.w3c.dom.Text

class ProductActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var favoritesRepository: FavoritesRepository
    private lateinit var btn_return: Button
    private lateinit var btn_add_cart: Button
    private lateinit var btn_add_fav: ImageView
    private lateinit var product_img: ImageView
    private lateinit var product_name: TextView
    private lateinit var product_price: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        //inicializamos variables
        db = DBHelper(this)
        productRepository = ProductRepository(db)
        cartRepository = CartRepository(db)
        favoritesRepository = FavoritesRepository(db)
        btn_return = findViewById(R.id.product_btn_return)
        btn_add_cart = findViewById(R.id.product_btn_add_cart)
        btn_add_fav = findViewById(R.id.product_btn_add_fav)
        product_img = findViewById(R.id.product_img)
        product_name = findViewById(R.id.product_name)
        product_price = findViewById(R.id.product_price)

        val productId = intent.getIntExtra("id_product",-1)
        val userId = intent.getIntExtra("id_user",-1)
        val product = productRepository.getProductById(productId)

        if (product != null){
            val resourceId = this.resources.getIdentifier(product.image, "drawable", this.packageName)
            product_img.setImageResource(resourceId)
            product_name.text = product.name
            product_price.text = "${product.price}"
        }

        btn_return.setOnClickListener{
            finish()
        }

        btn_add_cart.setOnClickListener {
            if (userId != -1 && product != null){
                val result = cartRepository.addToCart(userId, product.id)
                if (result == -1L) {
                    Toast.makeText(applicationContext ,"No se pudo agregar al carrito", Toast.LENGTH_LONG).show()
                }
                Toast.makeText(applicationContext ,"El producto ha sido añadido a tu carrito", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext ,"Inicia sesión", Toast.LENGTH_LONG).show()
            }
        }

        btn_add_fav.setOnClickListener {
            if (userId != -1 && product != null){
                val result = favoritesRepository.addToFavorites(userId, product.id)
                if (result == -1L) {
                    Toast.makeText(applicationContext ,"No se pudo agregar a favoritos", Toast.LENGTH_LONG).show()
                }
                Toast.makeText(applicationContext ,"El producto ha sido añadido a favoritos", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext ,"Inicia sesión", Toast.LENGTH_LONG).show()
            }
        }
    }
}