package com.tuapp.myapplication.data.models.savingsModels.request

data class CreateOrUpdateSavingDomain(
    val monto: Double,
    val mes: Int,
    val anio: Int,
)
