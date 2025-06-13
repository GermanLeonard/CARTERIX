package com.tuapp.myapplication.data.models.authModels.response

import com.tuapp.myapplication.data.models.authModels.UserDataDomain

data class LoginResponseDomain(
    val success: Boolean,
    val token: String,
    val datos_user: UserDataDomain,
)