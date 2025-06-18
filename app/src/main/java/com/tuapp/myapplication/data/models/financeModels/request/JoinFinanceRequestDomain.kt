package com.tuapp.myapplication.data.models.financeModels.request

import com.tuapp.myapplication.data.remote.request.financesRequest.JoinFinanceRequest

data class JoinFinanceRequestDomain(
    val codigo: String
)

fun JoinFinanceRequestDomain.toRequest(): JoinFinanceRequest {
    return JoinFinanceRequest(codigo)
}
