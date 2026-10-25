package com.example.pm1_t12_0285.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pm1_t12_0285.R
import com.example.pm1_t12_0285.model.Movimiento

class MovimientoAdapter(
    private var movimientos: List<Movimiento>
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    class MovimientoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProducto: TextView = view.findViewById(R.id.tvProductoMovimiento)
        val tvTipoCantidad: TextView = view.findViewById(R.id.tvTipoCantidadMovimiento)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaMovimiento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimiento, parent, false)
        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = movimientos[position]
        holder.tvProducto.text = movimiento.nombreProducto ?: "Producto #${movimiento.idProducto}"

        if (movimiento.tipo == "ENTRADA") {
            holder.tvTipoCantidad.text = "ENTRADA: +${movimiento.cantidad}"
            holder.tvTipoCantidad.setTextColor(Color.parseColor("#2E7D32")) // Verde
        } else {
            holder.tvTipoCantidad.text = "SALIDA: -${movimiento.cantidad}"
            holder.tvTipoCantidad.setTextColor(Color.parseColor("#C62828")) // Rojo
        }

        holder.tvFecha.text = movimiento.fecha
    }

    override fun getItemCount() = movimientos.size

    fun actualizarLista(nuevaLista: List<Movimiento>) {
        movimientos = nuevaLista
        notifyDataSetChanged()
    }
}
