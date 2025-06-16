package com.tuapp.myapplication.data.remote.responses.transactionsResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsDetailsDomain

data class TransactionDetailsResponse(
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("descripcion_gasto")
    val descripcion_gasto: String,
    @SerializedName("monto")
    val monto: Double,
    @SerializedName("movimiento")
    val movimiento: String,
    @SerializedName("nombre_usuario")
    val nombre_usuario: String,
    @SerializedName("presupuesto")
    val presupuesto: Double,
    @SerializedName("tipo_gasto")
    val tipo_gasto: String,
    @SerializedName("tipo_movimiento")
    val tipo_movimiento: String,
    @SerializedName("tipo_movimiento_id")
    val tipo_movimiento_id: Int
)

fun TransactionDetailsResponse.toDomain(): TransactionsDetailsDomain {
    return TransactionsDetailsDomain(
        categoria,
        descripcion_gasto,
        monto,
        movimiento,
        nombre_usuario,
        presupuesto,
        tipo_gasto,
        tipo_movimiento,
        tipo_movimiento_id
    )
}