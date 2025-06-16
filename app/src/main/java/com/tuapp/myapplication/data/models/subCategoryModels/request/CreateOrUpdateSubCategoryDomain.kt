package com.tuapp.myapplication.data.models.subCategoryModels.request

import com.tuapp.myapplication.data.remote.request.subCategoriesRequest.CreateOrUpdateSubCategoryRequest

data class CreateOrUpdateSubCategoryDomain(
    val categoria_id: Int,
    val nombre_sub_categoria: String,
    val presupuesto: Double,
    val tipo_gasto_id: Int
)

fun CreateOrUpdateSubCategoryDomain.toRequest(): CreateOrUpdateSubCategoryRequest {
    return CreateOrUpdateSubCategoryRequest(
        categoria_id,
        nombre_sub_categoria,
        presupuesto,
        tipo_gasto_id
    )
}
