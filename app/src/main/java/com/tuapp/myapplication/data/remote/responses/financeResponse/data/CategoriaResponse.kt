package com.tuapp.myapplication.data.remote.responses.financeResponse.data

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.CategorieDataEntity
import com.tuapp.myapplication.data.models.financeModels.CategorieResponseDomain

data class CategoriaResponse(
    @SerializedName("categoria_nombre")
    val categoria_nombre: String,
    @SerializedName("diferencia")
    val diferencia: Float,
    @SerializedName("gasto")
    val gasto: Float,
    @SerializedName("total_presupuesto")
    val total_presupuesto: Float
)

fun CategoriaResponse.toEntity(): CategorieDataEntity {
    return CategorieDataEntity(
        categoriaNombre = categoria_nombre,
        diferencia = diferencia,
        gasto = gasto,
        totalPresupuesto = total_presupuesto,
    )
}
