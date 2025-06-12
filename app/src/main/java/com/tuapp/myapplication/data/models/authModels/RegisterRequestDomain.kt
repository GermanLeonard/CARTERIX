package com.tuapp.myapplication.data.models.authModels

import com.tuapp.myapplication.data.remote.request.authRequest.RegisterRequest

data class RegisterRequestDomain(
    val nombre: String,
    val correo: String,
    val contrasena: String
)

fun RegisterRequestDomain.toRequest(): RegisterRequest {
    return RegisterRequest(
        nombre,
        correo,
        contrasena,
    )
}
