package com.tuapp.myapplication.data.database.entities.finance

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tuapp.myapplication.data.models.financeModels.CategorieResponseDomain

@Entity(tableName = "categorias_data")
data class CategorieDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val finanzaId: Int,

    val categoriaNombre: String,
    val totalPresupuesto: Float,
    val gasto: Float,
    val diferencia: Float,
)

fun CategorieDataEntity.toDomain(): CategorieResponseDomain {
    return CategorieResponseDomain(
        finanzaId,
        categoriaNombre,
        diferencia,
        gasto,
        totalPresupuesto,
    )
}
