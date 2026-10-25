package com.example.pm1_t12_0285

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pm1_t12_0285.database.MovimientoDao
import com.example.pm1_t12_0285.database.ProductoDao
import com.example.pm1_t12_0285.model.Movimiento
import com.example.pm1_t12_0285.model.Producto
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovimientoFormActivity : AppCompatActivity() {

    private lateinit var productoDao: ProductoDao
    private lateinit var movimientoDao: MovimientoDao

    private lateinit var spinnerProductos: Spinner
    private lateinit var spinnerTipo: Spinner
    private lateinit var etCantidad: TextInputEditText
    private lateinit var btnGuardar: Button

    private var listaProductos: List<Producto> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento_form)

        productoDao = ProductoDao(this)
        movimientoDao = MovimientoDao(this)

        spinnerProductos = findViewById(R.id.spinnerProductos)
        spinnerTipo = findViewById(R.id.spinnerTipoMovimiento)
        etCantidad = findViewById(R.id.etCantidadMovimiento)
        btnGuardar = findViewById(R.id.btnGuardarMovimiento)

        cargarProductosSpinner()
        cargarTiposSpinner()

        btnGuardar.setOnClickListener {
            registrarMovimiento()
        }
    }

    private fun cargarProductosSpinner() {
        listaProductos = productoDao.obtenerTodos()
        val nombres = listaProductos.map { "${it.nombre} (Stock: ${it.stock})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductos.adapter = adapter
    }

    private fun cargarTiposSpinner() {
        val tipos = listOf("ENTRADA", "SALIDA")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapter
    }

    private fun registrarMovimiento() {
        if (listaProductos.isEmpty()) {
            Toast.makeText(this, "No hay productos registrados", Toast.LENGTH_SHORT).show()
            return
        }

        val position = spinnerProductos.selectedItemPosition
        if (position == -1 || position >= listaProductos.size) {
            Toast.makeText(this, "Seleccione un producto válido", Toast.LENGTH_SHORT).show()
            return
        }

        val productoSeleccionado = listaProductos[position]
        val tipo = spinnerTipo.selectedItem.toString()
        val cantidadStr = etCantidad.text.toString().trim()

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese la cantidad", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull() ?: 0
        if (cantidad <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fechaActual = sdf.format(Date())

        val movimiento = Movimiento(
            idProducto = productoSeleccionado.id,
            cantidad = cantidad,
            tipo = tipo,
            fecha = fechaActual
        )

        val exito = movimientoDao.registrarMovimiento(movimiento)
        if (exito) {
            Toast.makeText(this, "Movimiento registrado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar. Verifique que haya stock suficiente para salidas.", Toast.LENGTH_LONG).show()
        }
    }
}
