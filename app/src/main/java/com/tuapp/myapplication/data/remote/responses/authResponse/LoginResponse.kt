package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain
import com.tuapp.myapplication.data.models.authModels.UserDataDomain

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("datos_user")
    val datos_user: UserDataResponse? = null,
    @SerializedName("message")
    val message: String? = null,
)

fun LoginResponse.toDomain(): LoginResponseDomain{
    return LoginResponseDomain(
        success,
        token,
        datos_user = datos_user?.let {  UserDataDomain(
            id = it.id,
            finanzaId = it.finanza_id,
            nombre = it.nombre,
            correo = it.correo,
        )},
        message,
    )
}