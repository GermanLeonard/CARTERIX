package com.tuapp.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuapp.myapplication.data.models.Ingreso
import com.tuapp.myapplication.data.repository.IngresoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class IngresoViewModel(private val repo: IngresoRepository) : ViewModel() {
    val ingresos: StateFlow<List<Ingreso>> = repo.ingresos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregar(ingreso: Ingreso) {
        viewModelScope.launch {
            repo.insertar(ingreso)
        }
    }

    fun eliminar(ingreso: Ingreso) {
        viewModelScope.launch {
            repo.eliminar(ingreso)
        }
    }
}

class IngresoViewModelFactory(private val repo: IngresoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngresoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngresoViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
