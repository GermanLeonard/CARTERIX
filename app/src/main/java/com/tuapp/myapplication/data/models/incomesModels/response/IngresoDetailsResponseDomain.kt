package com.tuapp.myapplication.data.models.incomesModels.response

data class IngresoDetailsResponseDomain(
    val descripcion_ingreso: String,
    val id_ingreso: Int,
    val monto_ingreso: Double,
    val nombre_ingreso: String
)
