package com.tuapp.myapplication.data.models.authModels

data class LoginResponseDomain(
    val success: Boolean,
    val token: String? = null,
    val message: String? = null,
)
