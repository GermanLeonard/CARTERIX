package com.tuapp.myapplication.data.remote.request.financesRequest

import com.google.gson.annotations.SerializedName

data class CreateFinanceRequest(
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("descripcion")
    val descripcion: String
)
