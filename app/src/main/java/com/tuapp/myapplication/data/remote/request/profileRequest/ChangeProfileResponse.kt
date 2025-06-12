package com.tuapp.myapplication.data.remote.request.profileRequest

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.authModels.UserDataDomain
import com.tuapp.myapplication.data.models.authModels.editProfile.ChangeProfileResponseDomain
import com.tuapp.myapplication.data.remote.responses.authResponse.UserDataResponse

data class ChangeProfileResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("datos_user")
    val datos_user: UserDataResponse? = null
)

fun ChangeProfileResponse.toDomain(): ChangeProfileResponseDomain {
    return ChangeProfileResponseDomain(
        success,
        message,
        datos_user = datos_user?.let {
            UserDataDomain(
                id = it.id,
                nombre = it.nombre,
                correo = it.correo
            )
        },
    )
}
