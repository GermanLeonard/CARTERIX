package com.tuapp.myapplication.data.remote.responses.categorieResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.categoryModels.response.CategorieDataResponseDomain
import com.tuapp.myapplication.data.models.categoryModels.response.SubCategoriesDomain

data class CategorieDataResponse(
    @SerializedName("diferencia_total")
    val diferencia_total: Double,
    @SerializedName("gasto_total")
    val gasto_total: Double,
    @SerializedName("presupuesto_total")
    val presupuesto_total: Double,
    @SerializedName("sub_categorias")
    val sub_categorias: List<SubCategories>
)

data class SubCategories(
    @SerializedName("diferencia_sub_categoria")
    val diferencia_sub_categoria: Double,
    @SerializedName("gasto_sub_categoria")
    val gasto_sub_categoria: Double,
    @SerializedName("nombre_sub_categoria")
    val nombre_sub_categoria: String,
    @SerializedName("presupuesto_sub_categoria")
    val presupuesto_sub_categoria: Double
)

fun SubCategories.toDomain(): SubCategoriesDomain {
    return SubCategoriesDomain(
        diferencia_sub_categoria,
        gasto_sub_categoria,
        nombre_sub_categoria,
        presupuesto_sub_categoria
    )
}

fun CategorieDataResponse.toDomain(): CategorieDataResponseDomain {
    return CategorieDataResponseDomain(
        diferencia_total,
        gasto_total,
        presupuesto_total,
        sub_categorias = sub_categorias.map { it.toDomain() }
    )
}