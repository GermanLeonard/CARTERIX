package com.tuapp.myapplication.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.tuapp.myapplication.data.models.CommonResponseDomain

data class CommonResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
)

fun CommonResponse.toDomain(): CommonResponseDomain {
    return CommonResponseDomain(
        success,
        message,
    )
}
