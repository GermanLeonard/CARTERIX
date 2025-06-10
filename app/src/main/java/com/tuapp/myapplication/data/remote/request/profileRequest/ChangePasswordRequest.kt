package com.tuapp.myapplication.data.remote.request.profileRequest

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("contrasena")
    val contrasena: String,
    @SerializedName("nueva_contrasena")
    val nueva_contrasena: String,
    @SerializedName("confirmar_contrasena")
    val confirmar_contrasena: String,
)
