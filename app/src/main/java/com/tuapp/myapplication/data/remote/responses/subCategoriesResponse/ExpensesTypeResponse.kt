package com.tuapp.myapplication.data.remote.responses.subCategoriesResponse

import com.google.gson.annotations.SerializedName

data class ExpensesTypeResponse (
    @SerializedName("opciones")
    val opciones: List<Options>
)

data class Options(
    @SerializedName("tipo_id")
    val tipo_id: Int,
    @SerializedName("tipo_nombre")
    val tipo_nombre: String
)