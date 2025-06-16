package com.tuapp.myapplication.data.remote.responses.incomesResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.income.IncomesEntity

data class IncomesListResponse(
    @SerializedName("ingresos")
    val ingresos: List<IngresoResponse>,
    @SerializedName("success")
    val success: Boolean
)

data class IngresoResponse(
    @SerializedName("finanza_id")
    val finanza_id: Int,
    @SerializedName("id_ingreso")
    val id_ingreso: Int,
    @SerializedName("monto_ingreso")
    val monto_ingreso: Double,
    @SerializedName("nombre_ingreso")
    val nombre_ingreso: String,
    @SerializedName("nombre_usuario")
    val nombre_usuario: String
)

fun IngresoResponse.toEntity(): IncomesEntity {
    return IncomesEntity(
        finanzaId = finanza_id,
        ingresoId = id_ingreso,
        montoIngreso = monto_ingreso,
        nombreIngreso = nombre_ingreso,
        nombreUsuario = nombre_usuario
    )
}

fun IncomesListResponse.toEntity(): List<IncomesEntity> {
    return ingresos.map { it.toEntity() }
}