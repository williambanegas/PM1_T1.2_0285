package com.example.pm1_t12_0285

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pm1_t12_0285.adapter.ProductoAdapter
import com.example.pm1_t12_0285.database.ProductoDao
import com.example.pm1_t12_0285.model.Producto
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductoActivity : AppCompatActivity() {

    private lateinit var productoDao: ProductoDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)
        supportActionBar?.title = "Gestión de Productos"

        productoDao = ProductoDao(this)
        recyclerView = findViewById(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fabAgregarProducto)
        fab.setOnClickListener {
            val intent = Intent(this, ProductoFormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    private fun cargarProductos() {
        val lista = productoDao.obtenerTodos()
        adapter = ProductoAdapter(
            lista,
            onItemClick = { producto ->
                val intent = Intent(this, ProductoFormActivity::class.java).apply {
                    putExtra("PRODUCTO_ID", producto.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { producto ->
                confirmarEliminacion(producto)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun confirmarEliminacion(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Producto")
            .setMessage("¿Estás seguro de eliminar '${producto.nombre}'? Se eliminarán también sus movimientos asociados.")
            .setPositiveButton("Eliminar") { _, _ ->
                productoDao.eliminar(producto.id)
                cargarProductos()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
