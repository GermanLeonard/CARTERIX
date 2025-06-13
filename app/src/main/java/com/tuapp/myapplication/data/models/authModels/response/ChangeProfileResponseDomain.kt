package com.tuapp.myapplication.data.models.authModels.response

import com.tuapp.myapplication.data.models.authModels.UserDataDomain

data class ChangeProfileResponseDomain(
    val success: Boolean,
    val datos_user: UserDataDomain,
    val message: String,
)