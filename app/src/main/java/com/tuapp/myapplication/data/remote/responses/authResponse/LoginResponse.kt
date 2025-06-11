package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.authModels.LoginResponseDomain

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("message")
    val message: String? = null,
)

fun LoginResponse.toDomain(): LoginResponseDomain{
    return LoginResponseDomain(
        success,
        token,
        message,
    )
}