package com.tuapp.myapplication.data.models.transactionsModels.request

import com.tuapp.myapplication.data.remote.request.transactionRequest.CreateTransactionRequest

data class CreateTransactionDomain(
    val descripcion: String,
    val fecha_registro: String,
    val monto: Double,
    val tipo_movimiento: Int,
    val tipo_transaccion: Int
)


fun CreateTransactionDomain.toRequest(): CreateTransactionRequest {
    return CreateTransactionRequest(
        descripcion,
        fecha_registro,
        monto,
        tipo_movimiento,
        tipo_transaccion,
    )
}