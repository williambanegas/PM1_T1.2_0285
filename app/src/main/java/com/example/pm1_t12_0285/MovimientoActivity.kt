package com.example.pm1_t12_0285

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pm1_t12_0285.adapter.MovimientoAdapter
import com.example.pm1_t12_0285.database.MovimientoDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MovimientoActivity : AppCompatActivity() {

    private lateinit var movimientoDao: MovimientoDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimiento)
        supportActionBar?.title = "Movimientos de Inventario"

        movimientoDao = MovimientoDao(this)
        recyclerView = findViewById(R.id.recyclerViewMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fabAgregarMovimiento)
        fab.setOnClickListener {
            val intent = Intent(this, MovimientoFormActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarMovimientos()
    }

    private fun cargarMovimientos() {
        val lista = movimientoDao.obtenerTodos()
        adapter = MovimientoAdapter(lista)
        recyclerView.adapter = adapter
    }
}
