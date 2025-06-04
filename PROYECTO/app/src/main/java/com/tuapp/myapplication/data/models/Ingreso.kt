package com.tuapp.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingreso(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val monto: Double
)
