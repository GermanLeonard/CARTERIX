package com.tuapp.myapplication.data.remote.request.transactionRequest

import com.google.gson.annotations.SerializedName

data class CreateTransactionRequest(
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("fecha_registro")
    val fecha_registro: String,
    @SerializedName("monto")
    val monto: Double,
    @SerializedName("tipo_movimiento")
    val tipo_movimiento: Int,
    @SerializedName("tipo_transaccion")
    val tipo_transaccion: Int
)