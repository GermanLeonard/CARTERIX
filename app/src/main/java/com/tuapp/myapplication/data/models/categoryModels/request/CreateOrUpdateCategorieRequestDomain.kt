package com.tuapp.myapplication.data.models.categoryModels.request

import com.tuapp.myapplication.data.remote.request.categorieRequest.CreateOrUpdateCategorieRequest

data class CreateOrUpdateCategorieRequestDomain(
    val nombre_categoria: String
)

fun CreateOrUpdateCategorieRequestDomain.toRequest(): CreateOrUpdateCategorieRequest {
    return CreateOrUpdateCategorieRequest(
        nombre_categoria
    )
}