package com.tuapp.myapplication.data.database.entities.category

import  androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesListDomain

@Entity(tableName = "categorias_egreso")
data class CategoriaEgresoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoriaId: Int,
    val finanzaId: Int,
    val nombreCategoria: String,
    val usuarioRegistro: String,
)

fun CategoriaEgresoEntity.toDomain(): CategoriesListDomain {
    return CategoriesListDomain(
        categoriaId,
        nombreCategoria,
        usuarioRegistro
    )
}