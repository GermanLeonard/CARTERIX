package com.tuapp.myapplication.data.remote.responses.incomesResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoDetailsResponseDomain

data class IncomeDetailsResponse(
    @SerializedName("ingreso")
    val ingreso: IngresoDetailsResponse,
    @SerializedName("success")
    val success: Boolean
)

data class IngresoDetailsResponse(
    @SerializedName("descripcion_ingreso")
    val descripcion_ingreso: String,
    @SerializedName("id_ingreso")
    val id_ingreso: Int,
    @SerializedName("monto_ingreso")
    val monto_ingreso: Double,
    @SerializedName("nombre_ingreso")
    val nombre_ingreso: String
)

fun IngresoDetailsResponse.toDomain(): IngresoDetailsResponseDomain {
    return IngresoDetailsResponseDomain(
        descripcion_ingreso,
        id_ingreso,
        monto_ingreso,
        nombre_ingreso
    )
}