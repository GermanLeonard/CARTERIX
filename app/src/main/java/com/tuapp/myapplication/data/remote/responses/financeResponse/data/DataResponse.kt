package com.tuapp.myapplication.data.remote.responses.financeResponse.data

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.database.entities.finance.CategorieDataEntity

data class DataResponse(
    @SerializedName("categorias")
    val categorias: List<CategoriaResponse>,
    @SerializedName("finanza_id")
    val finanza_id: Int? = null,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null
)

fun DataResponse.toEntityList(): List<CategorieDataEntity> {
    return this.categorias.map { it.toEntity() }
}