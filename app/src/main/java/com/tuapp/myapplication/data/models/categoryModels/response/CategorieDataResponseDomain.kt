package com.tuapp.myapplication.data.models.categoryModels.response

data class CategorieDataResponseDomain(
    val diferencia_total: Double,
    val gasto_total: Double,
    val presupuesto_total: Double,
    val sub_categorias: List<SubCategoriesDomain>
)

data class SubCategoriesDomain(
    val diferencia_sub_categoria: Double,
    val gasto_sub_categoria: Double,
    val nombre_sub_categoria: String,
    val presupuesto_sub_categoria: Double
)
