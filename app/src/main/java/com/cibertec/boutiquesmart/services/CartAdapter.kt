package com.cibertec.boutiquesmart.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.databinding.ItemCartBinding
import com.cibertec.boutiquesmart.model.Cart
import com.cibertec.boutiquesmart.model.Product

class CartAdapter(private val items: List<Cart>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtProductName.text = item.product.name
        holder.binding.txtProductPrice.text = "S/%.2f".format(item.product.price)
        holder.binding.txtProductCategory.text = item.product.category.name
    }

    override fun getItemCount(): Int = items.size
}
