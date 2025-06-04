package com.tuapp.myapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias_egreso")
data class CategoriaEgreso(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)
