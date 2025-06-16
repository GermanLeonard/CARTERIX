package com.tuapp.myapplication.data.models.transactionsModels.response

data class TransactionListResponseDomain(
    val fecha_transaccion: String,
    val monto_transaccion: Double,
    val nombre_categoria: String,
    val nombre_usuario: String,
    val tipo_movimiento_id: Int,
    val tipo_movimiento_nombre: String,
    val transaccion_id: Int
)
