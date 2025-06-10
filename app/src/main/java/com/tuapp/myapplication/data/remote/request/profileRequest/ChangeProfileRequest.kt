package com.tuapp.myapplication.data.remote.request.profileRequest

import com.google.gson.annotations.SerializedName

data class ChangeProfileRequest(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("correo")
    val correo: String,
)
