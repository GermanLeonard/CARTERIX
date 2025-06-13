package com.tuapp.myapplication.data.remote.responses.subCategoriesResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.subCategoryModels.response.SubCategoriaDomain

data class SubCategoryDetailsResponse(
    @SerializedName("sub_categoria")
    val sub_categoria: SubCategoria,
    @SerializedName("success")
    val success: Boolean
)

data class SubCategoria(
    @SerializedName("categoria_id")
    val categoria_id: Int,
    @SerializedName("nombre_sub_categoria")
    val nombre_sub_categoria: String,
    @SerializedName("presupuesto")
    val presupuesto: Double,
    @SerializedName("tipo_gasto_id")
    val tipo_gasto_id: Int
)

fun SubCategoryDetailsResponse.toDomain(): SubCategoriaDomain {
    return sub_categoria.let {
        SubCategoriaDomain(
            it.categoria_id,
            it.nombre_sub_categoria,
            it.presupuesto,
            it.tipo_gasto_id,
        )
    }
}
