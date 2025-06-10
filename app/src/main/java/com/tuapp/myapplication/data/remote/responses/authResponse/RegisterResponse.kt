package com.tuapp.myapplication.data.remote.responses.authResponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
)
