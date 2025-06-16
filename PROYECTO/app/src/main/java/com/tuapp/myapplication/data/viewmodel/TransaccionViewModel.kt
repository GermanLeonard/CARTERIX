package com.tuapp.myapplication.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuapp.myapplication.data.models.Transaccion
import com.tuapp.myapplication.data.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class TransaccionViewModel(private val repo: TransaccionRepository) : ViewModel() {
    val transacciones: StateFlow<List<Transaccion>> = repo.transacciones
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregar(transaccion: Transaccion) {
        viewModelScope.launch {
            repo.insertar(transaccion)
        }
    }

    fun eliminar(transaccion: Transaccion) {
        viewModelScope.launch {
            repo.eliminar(transaccion)
        }
    }
    fun getById(id: Int): Flow<Transaccion?> {
        return repo.obtenerPorId(id)
    }

}

class TransaccionViewModelFactory(private val repo: TransaccionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaccionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransaccionViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
