package com.tuapp.myapplication.data.remote.request.incomesRequest

import com.google.gson.annotations.SerializedName

data class CreateOrUpdateIncomeRequest(
    @SerializedName("nombre_ingreso")
    val nombre_ingreso: String,
    @SerializedName("descripcion_ingreso")
    val descripcion_ingreso: String,
    @SerializedName("monto_ingreso")
    val monto_ingreso: Double
)
