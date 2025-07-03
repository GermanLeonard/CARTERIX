package com.tuapp.myapplication.data.remote.websocket

import com.google.gson.annotations.SerializedName

data class WebSocketEvent(
    @SerializedName("event")
    val event: String
)
