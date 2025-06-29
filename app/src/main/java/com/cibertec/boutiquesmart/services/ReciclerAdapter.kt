package com.cibertec.boutiquesmart.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.model.Product
import com.cibertec.boutiquesmart.view.ProductActivity

class RecyclerAdapter(
    private val context: Context,
    private val products: MutableList<Product>,
    private val userid: Int
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.grid_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(products[position], userid, context)
        }
    }

    override fun getItemCount(): Int = products.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val productName: TextView = view.findViewById(R.id.nameProduct)
        private val price: TextView = view.findViewById(R.id.priceProduct)
        private val image: ImageView = view.findViewById(R.id.imgProduct)

        fun bind(product: Product, userid: Int, context: Context) {
            val resourceId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
            productName.text = product.name
            price.text = product.price.toString().trim()
            image.setImageResource(resourceId)

            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("id_product", product.id)
                    putInt("id_user", userid)
                }

                val intent = Intent(context, ProductActivity::class.java).apply {
                    putExtras(bundle)
                }

                context.startActivity(intent)
            }
        }
    }
}