package com.example.pm1_t12_0285

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pm1_t12_0285.adapter.ProductoAdapter
import com.example.pm1_t12_0285.database.ProductoDao
import java.util.Locale

class ResumenActivity : AppCompatActivity() {

    private lateinit var productoDao: ProductoDao
    private lateinit var tvValorTotal: TextView
    private lateinit var recyclerViewTop5: RecyclerView
    private lateinit var recyclerViewStockMinimo: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)
        supportActionBar?.title = "Resumen y Estadísticas"

        productoDao = ProductoDao(this)
        tvValorTotal = findViewById(R.id.tvValorTotalInventario)
        recyclerViewTop5 = findViewById(R.id.recyclerViewTop5)
        recyclerViewStockMinimo = findViewById(R.id.recyclerViewStockMinimo)

        recyclerViewTop5.layoutManager = LinearLayoutManager(this)
        recyclerViewStockMinimo.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        cargarResumen()
    }

    private fun cargarResumen() {
        val valorTotal = productoDao.calcularValorTotalInventario()
        tvValorTotal.text = String.format(Locale.getDefault(), "Lps. %.2f", valorTotal)

        val top5 = productoDao.obtenerTop5PorValor()
        val adapterTop5 = ProductoAdapter(
            top5,
            showActions = false,
            showTotalValue = true
        )
        recyclerViewTop5.adapter = adapterTop5

        val stockMinimo = productoDao.obtenerStockMinimo()
        val adapterStockMinimo = ProductoAdapter(
            stockMinimo,
            showActions = false
        )
        recyclerViewStockMinimo.adapter = adapterStockMinimo
    }
}
