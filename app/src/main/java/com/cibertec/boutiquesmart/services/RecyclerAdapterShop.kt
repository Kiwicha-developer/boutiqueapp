package com.cibertec.boutiquesmart.services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.controller.DBHelper
import com.cibertec.boutiquesmart.model.Cart
import com.cibertec.boutiquesmart.repository.CartRepository
import com.cibertec.boutiquesmart.view.CartActivity
import com.cibertec.boutiquesmart.view.ProductActivity

class RecyclerAdapterShop(
    private val context: Context,
    private val carts: MutableList<Cart>,
    private val userid: Int = -1
) : RecyclerView.Adapter<RecyclerAdapterShop.ViewHolder>() {
    private lateinit var db: DBHelper
    private lateinit var cartRepository: CartRepository

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view,carts, this )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carts[position], userid, context)
    }

    override fun getItemCount() = carts.size

    class ViewHolder(view: View,
                     private val carts: MutableList<Cart>,
                     private val adapter: RecyclerAdapterShop,) : RecyclerView.ViewHolder(view) {
        private val productName: TextView = view.findViewById(R.id.itemName)
        private val price: TextView = view.findViewById(R.id.itemPrice)
        private val image: ImageView = view.findViewById(R.id.itemImage)
        private val deleteIcon: ImageView = view.findViewById(R.id.delete_icon)
        private val addIcon: ImageView = view.findViewById(R.id.btnIncrease)
        private val removeIcon: ImageView = view.findViewById(R.id.btnDecrease)
        private val quantity: TextView = view.findViewById(R.id.itemQuantity)

        fun bind(cart: Cart, userid: Int, context: Context) {
            productName.text = cart.product.name
            price.text = "S/ %.2f".format(cart.product.price)
            quantity.text = "Cantidad: ${cart.cantidad}"
            val resId = context.resources.getIdentifier(cart.product.image, "drawable", context.packageName)
            image.setImageResource(resId)

            val db = DBHelper(context)
            val cartRepository = CartRepository(db)

            itemView.setOnClickListener {
                val intent = Intent(context, ProductActivity::class.java).apply {
                    putExtra("",userid)
                }

                context.startActivity(intent)
            }


            addIcon.setOnClickListener {
                if (userid != -1) {
                    cartRepository.updateQuantity(userid, cart.product.id, cart.cantidad + 1)
                    cart.cantidad += 1
                    quantity.text = "Cantidad: ${cart.cantidad}"
                    adapter.notifyItemChanged(adapterPosition)

                    val activity = context as? CartActivity
                    activity?.actualizarTotal(carts)
                } else {
                    Toast.makeText(context, "Inicia sesión", Toast.LENGTH_SHORT).show()
                }
            }

            removeIcon.setOnClickListener {
                if (userid != -1) {
                    if (cart.cantidad > 1) {
                        cartRepository.updateQuantity(userid, cart.product.id, cart.cantidad - 1)
                        cart.cantidad -= 1
                        quantity.text = "Cantidad: ${cart.cantidad}"
                        adapter.notifyItemChanged(adapterPosition)

                        val activity = context as? CartActivity
                        activity?.actualizarTotal(carts)
                    } else {
                        Toast.makeText(context, "Cantidad mínima alcanzada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Inicia sesión", Toast.LENGTH_SHORT).show()
                }
            }

            deleteIcon.setOnClickListener {
                if (userid != -1) {
                    cartRepository.removeFromCart(userid,cart.product.id)

                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        carts.removeAt(position)
                        adapter.notifyItemRemoved(position)

                    }

                    val activity = context as? CartActivity
                    activity?.actualizarTotal(carts)

                    Toast.makeText(it.context, "El producto ha sido eliminado de tu carrito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(it.context, "Inicia sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
