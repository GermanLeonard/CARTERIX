package com.tuapp.myapplication.data.models.incomes.request

import com.tuapp.myapplication.data.remote.request.incomesRequest.CreateOrUpdateIncomeRequest

data class CreateOrUpdateIncomeRequestDomain(
    val nombre_ingreso: String,
    val descripcion_ingreso: String,
    val monto_ingreso: Double
)

fun CreateOrUpdateIncomeRequestDomain.toRequest(): CreateOrUpdateIncomeRequest {
    return CreateOrUpdateIncomeRequest(
        nombre_ingreso,
        descripcion_ingreso,
        monto_ingreso
    )
}
