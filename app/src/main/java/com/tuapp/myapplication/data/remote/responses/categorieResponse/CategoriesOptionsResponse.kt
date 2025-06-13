package com.tuapp.myapplication.data.remote.responses.categorieResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesOptionsDomain

data class CategoriesOptions(
    @SerializedName("categoria_id")
    val categoria_id: Int,
    @SerializedName("categoria_nombre")
    val categoria_nombre: String,
)

fun CategoriesOptions.toDomain(): CategoriesOptionsDomain {
    return CategoriesOptionsDomain(
        categoria_id,
        categoria_nombre
    )
}

data class CategoriesOptionsResponse (
    @SerializedName("categorias")
    val categorias: List<CategoriesOptions>
)

fun CategoriesOptionsResponse.toDomain(): List<CategoriesOptionsDomain> {
    return categorias.map { it.toDomain() }
}