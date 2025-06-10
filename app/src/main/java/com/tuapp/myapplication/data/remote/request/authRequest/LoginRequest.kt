package com.tuapp.myapplication.data.remote.request.authRequest

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo")
    val correo: String,
    @SerializedName("contrasena")
    val contrasena: String
)