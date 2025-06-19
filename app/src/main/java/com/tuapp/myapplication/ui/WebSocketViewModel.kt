package com.tuapp.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.remote.websocket.WebSocketClient
import com.tuapp.myapplication.data.repository.sensitive.SensitiveInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WebSocketViewModel(
    private val repository: SensitiveInfoRepository,
): ViewModel() {

    private val _transaccionTrigger = MutableStateFlow(0)
    val transaccionTrigger: StateFlow<Int> = _transaccionTrigger

    private val _ahorroTrigger = MutableStateFlow(0)
    val ahorroTrigger: StateFlow<Int> = _ahorroTrigger

    private val _resumenFinanzaTrigger = MutableStateFlow(0)
    val resumenFinanzaTrigger: StateFlow<Int> = _resumenFinanzaTrigger

    private val _datosFinanzaTrigger = MutableStateFlow(0)
    val datosFinanzaTrigger: StateFlow<Int> = _datosFinanzaTrigger

    fun startListening(finanzaId: Int) {
        WebSocketClient.connect(finanzaId, repository){ event ->
            when (event.event) {
                "lista_transacciones" -> _transaccionTrigger.update { it + 1 }
                "ahorro_finanza" -> _ahorroTrigger.update { it + 1 }
                "resumen_finanza" -> _resumenFinanzaTrigger.update { it + 1 }
                "datos_finanza" -> _datosFinanzaTrigger.update { it + 1 }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        WebSocketClient.disconnect()
    }

    fun stopListening() {
        WebSocketClient.disconnect()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                WebSocketViewModel(
                    application.appProvider.provideSensitiveInfoRepository()
                )
            }
        }
    }
}