package com.cibertec.boutiquesmart.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.model.Product

class DetailFragment: Fragment() {
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var imgProduct: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        productName = view.findViewById(R.id.nameCProduct)
        productPrice = view.findViewById(R.id.priceCProduct)
        imgProduct = view.findViewById(R.id.imgCProduct)

        return view
    }

    fun showProduct(product: Product){
        view?.visibility = View.VISIBLE
        productName.text = product.name
        productPrice.text = product.price.toString().trim()
        val resId = requireContext().resources.getIdentifier(product.image, "drawable", requireContext().packageName)
        imgProduct.setImageResource(resId)
    }
}