package com.tuapp.myapplication.data.models.financeModels.request

import com.tuapp.myapplication.data.remote.request.financesRequest.CreateFinanceRequest

data class CreateFinanceRequestDomain(
    val titulo: String,
    val descripcion: String
)

fun CreateFinanceRequestDomain.toRequest(): CreateFinanceRequest {
    return CreateFinanceRequest(
        titulo,
        descripcion
    )
}
