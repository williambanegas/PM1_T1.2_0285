package com.example.pm1_t12_0285.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pm1_t12_0285.R
import com.example.pm1_t12_0285.model.Producto
import java.util.Locale

class ProductoAdapter(
    private var productos: List<Producto>,
    private val showActions: Boolean = true,
    private val showTotalValue: Boolean = false,
    private val onItemClick: (Producto) -> Unit = {},
    private val onDeleteClick: (Producto) -> Unit = {}
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecioProducto)
        val tvTotalValor: TextView = view.findViewById(R.id.tvTotalValor)
        val tvStock: TextView = view.findViewById(R.id.tvStockProducto)
        val tvCodigo: TextView = view.findViewById(R.id.tvCodigoBarraProducto)
        val btnEditar: View = view.findViewById(R.id.btnEditarProducto)
        val btnEliminar: View = view.findViewById(R.id.btnEliminarProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvPrecio.text = "Precio: Lps. ${String.format(Locale.getDefault(), "%.2f", producto.precio)}"
        holder.tvStock.text = "Stock: ${producto.stock} (Min: ${producto.stockMinimo})"
        holder.tvCodigo.text = "Código: ${producto.codigoBarra.ifEmpty { "N/A" }}"

        if (showTotalValue) {
            val totalValor = producto.precio * producto.stock
            holder.tvTotalValor.text = "Total: Lps. ${String.format(Locale.getDefault(), "%.2f", totalValor)}"
            holder.tvTotalValor.visibility = View.VISIBLE
        } else {
            holder.tvTotalValor.visibility = View.GONE
        }

        // Alerta visual si stock <= stockMinimo
        if (producto.stock <= producto.stockMinimo) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFEBEE")) // Rojo claro
            holder.tvStock.setTextColor(Color.parseColor("#C62828"))
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.tvStock.setTextColor(Color.parseColor("#333333"))
        }

        if (showActions) {
            holder.btnEditar.visibility = View.VISIBLE
            holder.btnEliminar.visibility = View.VISIBLE
            holder.btnEditar.setOnClickListener { onItemClick(producto) }
            holder.btnEliminar.setOnClickListener { onDeleteClick(producto) }
        } else {
            holder.btnEditar.visibility = View.GONE
            holder.btnEliminar.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { onItemClick(producto) }
    }

    override fun getItemCount() = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}
