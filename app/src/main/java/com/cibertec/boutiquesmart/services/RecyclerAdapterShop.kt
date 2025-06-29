package com.cibertec.boutiquesmart.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.repository.CartRepository
import com.cibertec.boutiquesmart.repository.ProductRepository
import com.cibertec.boutiquesmart.view.ProductActivity

class RecyclerAdapterShop(
    private val context: Context,
    private val products: MutableList<Product>,
    private val userid: Int = -1
) : RecyclerView.Adapter<RecyclerAdapterShop.ViewHolder>() {
    private lateinit var db: DBHelper
    private lateinit var cartRepository: CartRepository

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view,products, this )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position], userid, context)
    }

    override fun getItemCount() = products.size

    class ViewHolder(view: View,
                     private val products: MutableList<Product>,
                     private val adapter: RecyclerAdapterShop,) : RecyclerView.ViewHolder(view) {
        private val productName: TextView = view.findViewById(R.id.itemName)
        private val price: TextView = view.findViewById(R.id.itemPrice)
        private val image: ImageView = view.findViewById(R.id.itemImage)
        private val deleteIcon: ImageView = view.findViewById(R.id.delete_icon)

        fun bind(product: Product, userid: Int, context: Context) {
            productName.text = product.name
            price.text = "@String/ S/ %.2f".format(product.price)
            val resId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
            image.setImageResource(resId)

            val db = DBHelper(context)
            val cartRepository = CartRepository(db)

            itemView.setOnClickListener {
                val intent = Intent(context, ProductActivity::class.java).apply {
                    putExtra("",userid)
                }

                context.startActivity(intent)
            }

            deleteIcon.setOnClickListener {
                if (userid != -1) {
                    cartRepository.removeFromCart(userid,product.id)

                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        products.removeAt(position)
                        adapter.notifyItemRemoved(position)
                    }

                    Toast.makeText(it.context, "El producto ha sido eliminado de tu carrito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(it.context, "Inicia sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
