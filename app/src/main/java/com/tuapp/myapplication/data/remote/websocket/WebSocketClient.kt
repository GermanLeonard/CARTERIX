package com.tuapp.myapplication.data.remote.websocket

import com.tuapp.myapplication.data.remote.interceptors.AuthInterceptor
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor

object WebSocketClient {
    private var webSocket: WebSocket? = null

    fun connect(
        finanzaId: Int,
        repository: SensitiveInfoRepository,
        onEvent: (WebSocketEvent) -> Unit,
    ) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(AuthInterceptor(repository))
            .build()

        val url = "wss://backend-aplicacion-de-finanzas.onrender.com/ws/finanza/$finanzaId"
        val request = Request.Builder()
            .url(url)
            .build()

        val listener = AppWebSocketListener(onEvent)

        webSocket = client.newWebSocket(request, listener)
    }

    fun disconnect() {
        webSocket?.close(1000, "Cerrado por el cliente")
    }
}
