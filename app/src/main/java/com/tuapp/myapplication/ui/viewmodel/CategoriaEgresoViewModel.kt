package com.tuapp.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuapp.myapplication.data.database.entities.category.CategoriaEgresoEntity
import com.tuapp.myapplication.data.repository.CategoriaEgresoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriaEgresoViewModel(private val repository: CategoriaEgresoRepository) : ViewModel() {

    val categorias: StateFlow<List<CategoriaEgresoEntity>> = repository.categorias
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregar(categoria: CategoriaEgresoEntity) {
        viewModelScope.launch {
            repository.insertar(categoria)
        }
    }

    fun eliminar(categoria: CategoriaEgresoEntity) {
        viewModelScope.launch {
            repository.eliminar(categoria)
        }
    }
}

class CategoriaEgresoViewModelFactory(private val repository: CategoriaEgresoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriaEgresoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriaEgresoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


