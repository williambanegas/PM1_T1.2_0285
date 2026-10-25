package com.example.pm1_t12_0285.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pulperia.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PRODUCTOS = "productos"
        const val COLUMN_PRODUCTO_ID = "id"
        const val COLUMN_PRODUCTO_NOMBRE = "nombre"
        const val COLUMN_PRODUCTO_PRECIO = "precio"
        const val COLUMN_PRODUCTO_STOCK = "stock"
        const val COLUMN_PRODUCTO_CODIGO_BARRA = "codigo_barra"
        const val COLUMN_PRODUCTO_STOCK_MINIMO = "stock_minimo"

        const val TABLE_MOVIMIENTOS = "movimientos"
        const val COLUMN_MOVIMIENTO_ID = "id"
        const val COLUMN_MOVIMIENTO_ID_PRODUCTO = "id_producto"
        const val COLUMN_MOVIMIENTO_CANTIDAD = "cantidad"
        const val COLUMN_MOVIMIENTO_TIPO = "tipo"
        const val COLUMN_MOVIMIENTO_FECHA = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableProductos = ("CREATE TABLE $TABLE_PRODUCTOS ("
                + "$COLUMN_PRODUCTO_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_PRODUCTO_NOMBRE TEXT NOT NULL, "
                + "$COLUMN_PRODUCTO_PRECIO REAL NOT NULL, "
                + "$COLUMN_PRODUCTO_STOCK INTEGER NOT NULL, "
                + "$COLUMN_PRODUCTO_CODIGO_BARRA TEXT, "
                + "$COLUMN_PRODUCTO_STOCK_MINIMO INTEGER NOT NULL)")

        val createTableMovimientos = ("CREATE TABLE $TABLE_MOVIMIENTOS ("
                + "$COLUMN_MOVIMIENTO_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_MOVIMIENTO_ID_PRODUCTO INTEGER NOT NULL, "
                + "$COLUMN_MOVIMIENTO_CANTIDAD INTEGER NOT NULL, "
                + "$COLUMN_MOVIMIENTO_TIPO TEXT NOT NULL, "
                + "$COLUMN_MOVIMIENTO_FECHA TEXT NOT NULL, "
                + "FOREIGN KEY($COLUMN_MOVIMIENTO_ID_PRODUCTO) REFERENCES $TABLE_PRODUCTOS($COLUMN_PRODUCTO_ID) ON DELETE CASCADE)")

        db?.execSQL(createTableProductos)
        db?.execSQL(createTableMovimientos)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIMIENTOS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTOS")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        if (db?.isReadOnly == false) {
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }
}
