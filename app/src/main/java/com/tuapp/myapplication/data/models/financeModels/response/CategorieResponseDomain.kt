    package com.tuapp.myapplication.data.models.financeModels.response

    data class CategorieResponseDomain(
        val finanza_id: Int,
        val categoria_nombre: String,
        val diferencia: Double,
        val gasto: Double,
        val total_presupuesto: Double
    )


