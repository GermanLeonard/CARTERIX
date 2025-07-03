package com.tuapp.myapplication.data.remote.request.financesRequest

import com.google.gson.annotations.SerializedName

data class JoinFinanceRequest(
    @SerializedName("codigo")
    val codigo: String
)
