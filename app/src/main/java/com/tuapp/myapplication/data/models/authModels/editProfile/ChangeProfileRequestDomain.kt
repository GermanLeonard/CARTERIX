package com.tuapp.myapplication.data.models.authModels.editProfile

import com.tuapp.myapplication.data.remote.request.profileRequest.ChangeProfileRequest

data class ChangeProfileRequestDomain(
    val nombre: String,
    val correo: String,
)

fun ChangeProfileRequestDomain.toRequest(): ChangeProfileRequest {
    return ChangeProfileRequest(
        nombre,
        correo
    )
}
