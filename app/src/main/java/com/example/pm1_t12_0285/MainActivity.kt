package com.example.pm1_t12_0285

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnProductos = findViewById<Button>(R.id.btnProductos)
        val btnMovimientos = findViewById<Button>(R.id.btnMovimientos)
        val btnResumen = findViewById<Button>(R.id.btnResumen)

        btnProductos.setOnClickListener {
            startActivity(Intent(this, ProductoActivity::class.java))
        }

        btnMovimientos.setOnClickListener {
            startActivity(Intent(this, MovimientoActivity::class.java))
        }

        btnResumen.setOnClickListener {
            startActivity(Intent(this, ResumenActivity::class.java))
        }
    }
}
