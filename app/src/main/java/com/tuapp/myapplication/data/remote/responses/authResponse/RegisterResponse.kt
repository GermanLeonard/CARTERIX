package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.authModels.RegisterResponseDomain

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
)

fun RegisterResponse.toDomain(): RegisterResponseDomain {
    return RegisterResponseDomain(
        success,
        message,
    )
}
