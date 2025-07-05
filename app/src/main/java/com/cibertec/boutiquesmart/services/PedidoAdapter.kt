package com.cibertec.boutiquesmart.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.boutiquesmart.R
import com.cibertec.boutiquesmart.model.Pedido

class PedidoAdapter(private val pedidos: List<Pedido>) :
    RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtProducto: TextView = view.findViewById(R.id.txtProducto)
        val txtCantidad: TextView = view.findViewById(R.id.txtCantidad)
        val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.txtProducto.text = pedido.producto
        holder.txtCantidad.text = "Cantidad: ${pedido.cantidad}"
        holder.txtPrecio.text = "S/ %.2f".format(
            pedido.precioUnitario.toFloat() * pedido.cantidad.toInt()
        )
        holder.txtEstado.text = "Estado: ${pedido.estado}"
        holder.txtFecha.text = "Fecha: ${pedido.fecha}"
    }

    override fun getItemCount(): Int = pedidos.size
}
