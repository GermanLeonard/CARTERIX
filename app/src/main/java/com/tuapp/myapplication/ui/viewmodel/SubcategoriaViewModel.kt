package com.tuapp.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tuapp.myapplication.data.models.Subcategoria
import com.tuapp.myapplication.data.repository.SubcategoriaRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class SubcategoriaViewModel(private val repo: SubcategoriaRepository) : ViewModel() {
    val subcategorias: StateFlow<List<Subcategoria>> = repo.subcategorias
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agregar(sub: Subcategoria) {
        viewModelScope.launch {
            repo.insertar(sub)
        }
    }

    fun eliminar(sub: Subcategoria) {
        viewModelScope.launch {
            repo.eliminar(sub)
        }
    }
}

class SubcategoriaViewModelFactory(private val repo: SubcategoriaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubcategoriaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubcategoriaViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

