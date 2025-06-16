package com.tuapp.myapplication.data.models.incomes.response

data class IngresoResponseDomain(
    val id_ingreso: Int,
    val monto_ingreso: Double,
    val nombre_ingreso: String,
    val nombre_usuario: String
)
