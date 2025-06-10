package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("token")
    val token: String?,
    @SerializedName("message")
    val message: String?,
)