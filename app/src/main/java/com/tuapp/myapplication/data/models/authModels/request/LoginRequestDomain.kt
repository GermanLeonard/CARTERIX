package com.tuapp.myapplication.data.models.authModels.request

import com.tuapp.myapplication.data.remote.request.authRequest.LoginRequest

data class LoginRequestDomain(
   val correo: String,
    val contrasena: String
)

fun LoginRequestDomain.toRequest(): LoginRequest {
    return LoginRequest(
        correo = correo,
        contrasena = contrasena
    )
}
