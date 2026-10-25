package com.example.pm1_t12_0285.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.pm1_t12_0285.model.Producto

class ProductoDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun insertar(producto: Producto): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PRODUCTO_NOMBRE, producto.nombre)
            put(DatabaseHelper.COLUMN_PRODUCTO_PRECIO, producto.precio)
            put(DatabaseHelper.COLUMN_PRODUCTO_STOCK, producto.stock)
            put(DatabaseHelper.COLUMN_PRODUCTO_CODIGO_BARRA, producto.codigoBarra)
            put(DatabaseHelper.COLUMN_PRODUCTO_STOCK_MINIMO, producto.stockMinimo)
        }
        val id = db.insert(DatabaseHelper.TABLE_PRODUCTOS, null, values)
        db.close()
        return id
    }

    fun actualizar(producto: Producto): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_PRODUCTO_NOMBRE, producto.nombre)
            put(DatabaseHelper.COLUMN_PRODUCTO_PRECIO, producto.precio)
            put(DatabaseHelper.COLUMN_PRODUCTO_STOCK, producto.stock)
            put(DatabaseHelper.COLUMN_PRODUCTO_CODIGO_BARRA, producto.codigoBarra)
            put(DatabaseHelper.COLUMN_PRODUCTO_STOCK_MINIMO, producto.stockMinimo)
        }
        val rows = db.update(
            DatabaseHelper.TABLE_PRODUCTOS,
            values,
            "${DatabaseHelper.COLUMN_PRODUCTO_ID} = ?",
            arrayOf(producto.id.toString())
        )
        db.close()
        return rows
    }

    fun eliminar(id: Long): Int {
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            DatabaseHelper.TABLE_PRODUCTOS,
            "${DatabaseHelper.COLUMN_PRODUCTO_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rows
    }

    fun obtenerTodos(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_PRODUCTOS,
            null, null, null, null, null,
            "${DatabaseHelper.COLUMN_PRODUCTO_NOMBRE} ASC"
        )
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToProducto(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun obtenerPorId(id: Long): Producto? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_PRODUCTOS,
            null,
            "${DatabaseHelper.COLUMN_PRODUCTO_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var producto: Producto? = null
        if (cursor.moveToFirst()) {
            producto = cursorToProducto(cursor)
        }
        cursor.close()
        db.close()
        return producto
    }

    fun obtenerStockMinimo(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_PRODUCTOS} WHERE ${DatabaseHelper.COLUMN_PRODUCTO_STOCK} <= ${DatabaseHelper.COLUMN_PRODUCTO_STOCK_MINIMO} ORDER BY ${DatabaseHelper.COLUMN_PRODUCTO_STOCK} ASC"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToProducto(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun obtenerTop5PorValor(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = dbHelper.readableDatabase
        val query = "SELECT *, (${DatabaseHelper.COLUMN_PRODUCTO_PRECIO} * ${DatabaseHelper.COLUMN_PRODUCTO_STOCK}) as valor_total FROM ${DatabaseHelper.TABLE_PRODUCTOS} ORDER BY valor_total DESC LIMIT 5"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToProducto(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun calcularValorTotalInventario(): Double {
        val db = dbHelper.readableDatabase
        val query = "SELECT SUM(${DatabaseHelper.COLUMN_PRODUCTO_PRECIO} * ${DatabaseHelper.COLUMN_PRODUCTO_STOCK}) FROM ${DatabaseHelper.TABLE_PRODUCTOS}"
        val cursor = db.rawQuery(query, null)
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }

    fun existeCodigoBarra(codigoBarra: String, excluirId: Long = -1L): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT ${DatabaseHelper.COLUMN_PRODUCTO_ID} FROM ${DatabaseHelper.TABLE_PRODUCTOS} WHERE ${DatabaseHelper.COLUMN_PRODUCTO_CODIGO_BARRA} = ? AND ${DatabaseHelper.COLUMN_PRODUCTO_ID} != ?"
        val cursor = db.rawQuery(query, arrayOf(codigoBarra, excluirId.toString()))
        val existe = cursor.moveToFirst()
        cursor.close()
        db.close()
        return existe
    }

    private fun cursorToProducto(cursor: Cursor): Producto {
        val idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_ID)
        val nomIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_NOMBRE)
        val preIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_PRECIO)
        val stoIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_STOCK)
        val codIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_CODIGO_BARRA)
        val minIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_STOCK_MINIMO)

        return Producto(
            id = if (idIdx != -1) cursor.getLong(idIdx) else 0,
            nombre = if (nomIdx != -1) cursor.getString(nomIdx) else "",
            precio = if (preIdx != -1) cursor.getDouble(preIdx) else 0.0,
            stock = if (stoIdx != -1) cursor.getInt(stoIdx) else 0,
            codigoBarra = if (codIdx != -1) cursor.getString(codIdx) ?: "" else "",
            stockMinimo = if (minIdx != -1) cursor.getInt(minIdx) else 0
        )
    }
}
