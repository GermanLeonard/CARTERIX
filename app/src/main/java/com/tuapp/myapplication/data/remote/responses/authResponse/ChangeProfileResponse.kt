package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.response.ChangeProfileResponseDomain

data class ChangeProfileResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("datos_user")
    val datos_user: UserDataResponse,
    @SerializedName("message")
    val message: String
)

fun ChangeProfileResponse.toDomain(): ChangeProfileResponseDomain {
    return ChangeProfileResponseDomain(
        success,
        datos_user = datos_user.let {
            UserDataDomain(
                id = it.id,
                finanzaId = it.finanza_id,
                nombre = it.nombre,
                correo = it.correo
            )
        },
        message,
    )
}
