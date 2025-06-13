package com.tuapp.myapplication.data.models.subCategoryModels.response

data class SubCategoriaDomain(
    val categoria_id: Int,
    val nombre_sub_categoria: String,
    val presupuesto: Double,
    val tipo_gasto_id: Int
)
