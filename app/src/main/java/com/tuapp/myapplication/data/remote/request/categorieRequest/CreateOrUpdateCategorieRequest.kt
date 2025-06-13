package com.tuapp.myapplication.data.remote.request.categorieRequest

import com.google.gson.annotations.SerializedName

data class CreateOrUpdateCategorieRequest(
    @SerializedName("nombre_categoria")
    val nombre_categoria: String
)
