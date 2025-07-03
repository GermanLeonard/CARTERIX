package com.tuapp.myapplication.data.remote.responses.financeResponse.conjunta

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.ConjFinanceEntity

data class FinanceListRespose(
    @SerializedName("finanzas")
    val finanzas: List<Finanza>,
    @SerializedName("success")
    val success: Boolean
)

data class Finanza(
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("finanza_nombre")
    val finanza_nombre: String,
    @SerializedName("nombre_admin")
    val nombre_admin: String
)

fun Finanza.toEntity(): ConjFinanceEntity {
    return ConjFinanceEntity(
        finanza_id,
        finanza_nombre,
        nombre_admin
    )
}

fun FinanceListRespose.toEntity(): List<ConjFinanceEntity> {
    return finanzas.map { it.toEntity() }
}