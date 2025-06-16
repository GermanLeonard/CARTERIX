package com.tuapp.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class Transaccion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val categoria: String,
    val monto: Double,
    val descripcion: String
)