package com.tuapp.myapplication.data.models.transactionsModels.response

data class TransactionsDetailsDomain(
    val categoria: String,
    val descripcion_gasto: String,
    val monto: Double,
    val movimiento: String,
    val nombre_usuario: String,
    val presupuesto: Double,
    val tipo_gasto: String,
    val tipo_movimiento: String,
    val tipo_movimiento_id: Int
)
