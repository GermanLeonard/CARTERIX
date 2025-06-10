package com.tuapp.myapplication.data.remote.request.authRequest

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("correo")
    val correo: String,
    @SerializedName("contrasena")
    val contrasena: String
)
