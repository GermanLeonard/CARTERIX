package com.tuapp.myapplication.data.models.financeModels

data class CategorieResponseDomain(
    val finanza_id: Int,
    val categoria_nombre: String,
    val diferencia: Float,
    val gasto: Float,
    val total_presupuesto: Float
)


