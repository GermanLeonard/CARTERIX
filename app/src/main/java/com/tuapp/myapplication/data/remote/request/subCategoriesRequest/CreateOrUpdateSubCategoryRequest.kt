package com.tuapp.myapplication.data.remote.request.subCategoriesRequest

import com.google.gson.annotations.SerializedName

data class CreateOrUpdateSubCategoryRequest(
    @SerializedName("categoria_id")
    val categoria_id: Int,
    @SerializedName("nombre_sub_categoria")
    val nombre_sub_categoria: String,
    @SerializedName("presupuesto")
    val presupuesto: Double,
    @SerializedName("tipo_gasto_id")
    val tipo_gasto_id: Int
)