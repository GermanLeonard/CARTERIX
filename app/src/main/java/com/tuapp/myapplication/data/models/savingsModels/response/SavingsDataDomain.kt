package com.tuapp.myapplication.data.models.savingsModels.response

data class SavingsDataDomain(
    val anio: Int,
    val mes: Int,
    val meta_ahorro: Double,
    val monto_ahorrado: Double,
    val nombre_mes: String,
    val porcentaje_cumplimiento: Double
)
