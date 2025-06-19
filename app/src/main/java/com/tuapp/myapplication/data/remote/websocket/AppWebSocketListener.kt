package com.tuapp.myapplication.data.remote.websocket

import android.util.Log
import com.google.gson.Gson
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class AppWebSocketListener(
    private val onEventReceived: (WebSocketEvent) -> Unit
): WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocket", "Conexion abierta")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocket", "Mensaje recibido $text")
        try {
            val gson = Gson()

            val event = gson.fromJson(text, WebSocketEvent::class.java)
            onEventReceived(event)
        } catch (e: Exception) {
            Log.e("WebSocket", "Error al parsear el evento: ${e.message}")
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Error en WebSocket: ${t.message}")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocket", "Conexión cerrada: $reason")
    }
}
