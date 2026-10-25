package com.example.pm1_t12_0285.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.pm1_t12_0285.model.Movimiento

class MovimientoDao(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun registrarMovimiento(movimiento: Movimiento): Boolean {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            // 1. Obtener producto actual para actualizar stock
            val prodCursor = db.query(
                DatabaseHelper.TABLE_PRODUCTOS,
                arrayOf(DatabaseHelper.COLUMN_PRODUCTO_STOCK),
                "${DatabaseHelper.COLUMN_PRODUCTO_ID} = ?",
                arrayOf(movimiento.idProducto.toString()),
                null, null, null
            )

            if (!prodCursor.moveToFirst()) {
                prodCursor.close()
                db.endTransaction()
                db.close()
                return false
            }

            val stockActual = prodCursor.getInt(0)
            prodCursor.close()

            val nuevoStock = if (movimiento.tipo == "ENTRADA") {
                stockActual + movimiento.cantidad
            } else {
                stockActual - movimiento.cantidad
            }

            if (nuevoStock < 0) {
                db.endTransaction()
                db.close()
                return false // Stock insuficiente para salida
            }

            // 2. Actualizar stock en productos
            val stockValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_PRODUCTO_STOCK, nuevoStock)
            }
            db.update(
                DatabaseHelper.TABLE_PRODUCTOS,
                stockValues,
                "${DatabaseHelper.COLUMN_PRODUCTO_ID} = ?",
                arrayOf(movimiento.idProducto.toString())
            )

            // 3. Insertar movimiento
            val movValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_MOVIMIENTO_ID_PRODUCTO, movimiento.idProducto)
                put(DatabaseHelper.COLUMN_MOVIMIENTO_CANTIDAD, movimiento.cantidad)
                put(DatabaseHelper.COLUMN_MOVIMIENTO_TIPO, movimiento.tipo)
                put(DatabaseHelper.COLUMN_MOVIMIENTO_FECHA, movimiento.fecha)
            }
            val idMov = db.insert(DatabaseHelper.TABLE_MOVIMIENTOS, null, movValues)

            if (idMov != -1L) {
                db.setTransactionSuccessful()
                db.endTransaction()
                db.close()
                return true
            }

            db.endTransaction()
            db.close()
            return false
        } catch (e: Exception) {
            db.endTransaction()
            db.close()
            return false
        }
    }

    fun obtenerTodos(): List<Movimiento> {
        val lista = mutableListOf<Movimiento>()
        val db = dbHelper.readableDatabase
        val query = "SELECT m.*, p.${DatabaseHelper.COLUMN_PRODUCTO_NOMBRE} FROM ${DatabaseHelper.TABLE_MOVIMIENTOS} m INNER JOIN ${DatabaseHelper.TABLE_PRODUCTOS} p ON m.${DatabaseHelper.COLUMN_MOVIMIENTO_ID_PRODUCTO} = p.${DatabaseHelper.COLUMN_PRODUCTO_ID} ORDER BY m.${DatabaseHelper.COLUMN_MOVIMIENTO_ID} DESC"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val idIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_MOVIMIENTO_ID)
                val prodIdIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_MOVIMIENTO_ID_PRODUCTO)
                val cantIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_MOVIMIENTO_CANTIDAD)
                val tipoIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_MOVIMIENTO_TIPO)
                val fechaIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_MOVIMIENTO_FECHA)
                val nomIdx = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRODUCTO_NOMBRE)

                lista.add(
                    Movimiento(
                        id = if (idIdx != -1) cursor.getLong(idIdx) else 0,
                        idProducto = if (prodIdIdx != -1) cursor.getLong(prodIdIdx) else 0,
                        nombreProducto = if (nomIdx != -1) cursor.getString(nomIdx) else "Desconocido",
                        cantidad = if (cantIdx != -1) cursor.getInt(cantIdx) else 0,
                        tipo = if (tipoIdx != -1) cursor.getString(tipoIdx) else "",
                        fecha = if (fechaIdx != -1) cursor.getString(fechaIdx) else ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
