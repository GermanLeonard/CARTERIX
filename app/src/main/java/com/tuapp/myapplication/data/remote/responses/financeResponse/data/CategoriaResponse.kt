package com.tuapp.myapplication.data.remote.responses.financeResponse.data

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.CategorieDataEntity

data class CategoriaResponse(
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("categoria_nombre")
    val categoria_nombre: String,
    @SerializedName("diferencia")
    val diferencia: Double,
    @SerializedName("gasto")
    val gasto: Double,
    @SerializedName("total_presupuesto")
    val total_presupuesto: Double
)

fun CategoriaResponse.toEntity(): CategorieDataEntity {
    return CategorieDataEntity(
        finanzaId = finanza_id,
        categoriaNombre = categoria_nombre,
        diferencia = diferencia,
        gasto = gasto,
        totalPresupuesto = total_presupuesto,
    )
}
