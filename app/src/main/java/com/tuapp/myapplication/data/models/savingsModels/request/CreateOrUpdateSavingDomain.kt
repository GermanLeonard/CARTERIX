package com.tuapp.myapplication.data.models.savingsModels.request

import com.tuapp.myapplication.data.remote.request.savingRequest.CreateOrUpdateSavingGoal

data class CreateOrUpdateSavingDomain(
    val monto: Double,
    val mes: Int,
    val anio: Int,
)

fun CreateOrUpdateSavingDomain.toRequest(): CreateOrUpdateSavingGoal {
    return CreateOrUpdateSavingGoal(
        monto,
        mes,
        anio
    )
}
