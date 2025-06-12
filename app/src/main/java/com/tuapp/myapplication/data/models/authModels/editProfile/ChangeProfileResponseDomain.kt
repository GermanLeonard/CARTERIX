package com.tuapp.myapplication.data.models.authModels.editProfile

import com.tuapp.myapplication.data.models.authModels.UserDataDomain

data class ChangeProfileResponseDomain(
    val success: Boolean,
    val message: String,
    val datos_user: UserDataDomain? = null,
)

