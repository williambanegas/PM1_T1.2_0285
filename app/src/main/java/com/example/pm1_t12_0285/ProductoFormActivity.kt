package com.example.pm1_t12_0285

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pm1_t12_0285.database.ProductoDao
import com.example.pm1_t12_0285.model.Producto
import com.google.android.material.textfield.TextInputEditText

class ProductoFormActivity : AppCompatActivity() {

    private lateinit var productoDao: ProductoDao
    private var productoId: Long = -1L

    private lateinit var tvTitulo: TextView
    private lateinit var etNombre: TextInputEditText
    private lateinit var etPrecio: TextInputEditText
    private lateinit var etStock: TextInputEditText
    private lateinit var etCodigoBarra: TextInputEditText
    private lateinit var etStockMinimo: TextInputEditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto_form)

        productoDao = ProductoDao(this)

        tvTitulo = findViewById(R.id.tvTituloFormProducto)
        etNombre = findViewById(R.id.etNombreProducto)
        etPrecio = findViewById(R.id.etPrecioProducto)
        etStock = findViewById(R.id.etStockProducto)
        etCodigoBarra = findViewById(R.id.etCodigoBarra)
        etStockMinimo = findViewById(R.id.etStockMinimo)
        btnGuardar = findViewById(R.id.btnGuardarProducto)

        productoId = intent.getLongExtra("PRODUCTO_ID", -1L)
        if (productoId != -1L) {
            tvTitulo.text = "Editar Producto"
            btnGuardar.text = "Actualizar Producto"
            cargarDatosProducto(productoId)
        } else {
            tvTitulo.text = "Registrar Producto"
            btnGuardar.text = "Guardar Producto"
        }

        btnGuardar.setOnClickListener {
            guardarProducto()
        }
    }

    private fun cargarDatosProducto(id: Long) {
        val producto = productoDao.obtenerPorId(id)
        producto?.let {
            etNombre.setText(it.nombre)
            etPrecio.setText(it.precio.toString())
            etStock.setText(it.stock.toString())
            etCodigoBarra.setText(it.codigoBarra)
            etStockMinimo.setText(it.stockMinimo.toString())
        }
    }

    private fun guardarProducto() {
        val nombre = etNombre.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()
        val stockStr = etStock.text.toString().trim()
        val codigoBarra = etCodigoBarra.text.toString().trim()
        val stockMinStr = etStockMinimo.text.toString().trim()

        var isValid = true

        if (nombre.isEmpty()) {
            etNombre.error = "El nombre del producto es obligatorio"
            isValid = false
        } else {
            etNombre.error = null
        }

        if (precioStr.isEmpty()) {
            etPrecio.error = "El precio es obligatorio"
            isValid = false
        } else {
            val precioVal = precioStr.toDoubleOrNull()
            if (precioVal == null || precioVal <= 0.0) {
                etPrecio.error = "Ingrese un precio válido mayor a 0"
                isValid = false
            } else {
                etPrecio.error = null
            }
        }

        if (stockStr.isEmpty()) {
            etStock.error = "El stock inicial es obligatorio"
            isValid = false
        } else {
            val stockVal = stockStr.toIntOrNull()
            if (stockVal == null || stockVal < 0) {
                etStock.error = "Ingrese un stock válido (0 o mayor)"
                isValid = false
            } else {
                etStock.error = null
            }
        }

        if (codigoBarra.isEmpty()) {
            etCodigoBarra.error = "El código de barra es obligatorio"
            isValid = false
        } else if (productoDao.existeCodigoBarra(codigoBarra, productoId)) {
            etCodigoBarra.error = "Este código de barra ya existe. Ingrese un código nuevo."
            isValid = false
        } else {
            etCodigoBarra.error = null
        }

        if (stockMinStr.isEmpty()) {
            etStockMinimo.error = "El stock mínimo es obligatorio"
            isValid = false
        } else {
            val stockMinVal = stockMinStr.toIntOrNull()
            if (stockMinVal == null || stockMinVal < 0) {
                etStockMinimo.error = "Ingrese un stock mínimo válido (0 o mayor)"
                isValid = false
            } else {
                etStockMinimo.error = null
            }
        }

        if (!isValid) {
            Toast.makeText(this, "Por favor corrija los errores en los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDouble()
        val stock = stockStr.toInt()
        val stockMinimo = stockMinStr.toInt()

        val producto = Producto(
            id = if (productoId != -1L) productoId else 0,
            nombre = nombre,
            precio = precio,
            stock = stock,
            codigoBarra = codigoBarra,
            stockMinimo = stockMinimo
        )

        if (productoId == -1L) {
            val id = productoDao.insertar(producto)
            if (id != -1L) {
                Toast.makeText(this, "Producto registrado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar producto", Toast.LENGTH_SHORT).show()
            }
        } else {
            val rows = productoDao.actualizar(producto)
            if (rows > 0) {
                Toast.makeText(this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
