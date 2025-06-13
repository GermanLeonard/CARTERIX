package com.tuapp.myapplication.data.database.entities.subCategory

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.subCategoryModels.response.ListaSubCategoriasDomain

@Entity(tableName = "sub_categoria_egresos")
data class SubCategoriaEgresoEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subCategoriaId: Int,
    val finanzaId: Int,
    val subCategoriaNombre: String,
    val categoriaNombre: String,
    val tipoGasto: String,
    val nombreUsuario: String,
    val presupuesto: Double,
)

fun SubCategoriaEgresoEntity.toDomain(): ListaSubCategoriasDomain {
    return ListaSubCategoriasDomain(
        finanzaId,
        categoriaNombre,
        nombreUsuario,
        presupuesto,
        subCategoriaId,
        subCategoriaNombre,
        tipoGasto
    )
}