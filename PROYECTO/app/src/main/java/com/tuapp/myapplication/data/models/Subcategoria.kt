package com.tuapp.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subcategoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String, 
    val presupuesto: Double,
    val categoriaPadre: String
)

