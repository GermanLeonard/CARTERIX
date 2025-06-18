package com.tuapp.myapplication.data.remote.responses.financeResponse.invitacion

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.financeModels.response.CreateInviteResponseDomain

data class CreateInviteResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("codigo_invitacion")
    val codigo_invitacion: String,
)

fun CreateInviteResponse.toDomain(): CreateInviteResponseDomain {
    return CreateInviteResponseDomain(
        success,
        codigo_invitacion
    )
}
