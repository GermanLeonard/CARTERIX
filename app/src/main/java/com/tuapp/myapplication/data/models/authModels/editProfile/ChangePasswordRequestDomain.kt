package com.tuapp.myapplication.data.models.authModels.editProfile

import com.tuapp.myapplication.data.remote.request.profileRequest.ChangePasswordRequest

data class ChangePasswordRequestDomain(
    val contrasena: String,
    val nueva_contrasena: String,
    val confirmar_contrasena: String
)

fun ChangePasswordRequestDomain.toRequest(): ChangePasswordRequest {
    return ChangePasswordRequest(
        contrasena,
        nueva_contrasena,
        confirmar_contrasena
    )
}
