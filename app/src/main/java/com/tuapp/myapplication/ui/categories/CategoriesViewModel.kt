package com.tuapp.myapplication.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.categoryModels.request.CreateOrUpdateCategorieRequestDomain
import com.tuapp.myapplication.data.repository.categories.CategoryRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val categoriesRepository: CategoryRepository
): ViewModel() {

    //CREEN USTEDES LOS ESTADOS QUE SERAN NECESARIOS A MOSTRAR EN LA VISTA
    // (lista de categorias, estados de cargando, mensajes de error, etc.)

    //PASAR EL ID SI ES CONJUNTA,
    //ESTAS SON LAS OPCIONES DE CATEGORIAS QUE SE VAN A MOSTRAR EN EL SELECT
    //PARA FILTRAR DATOS POR CATEGORIA
    fun getCategoriesOptions(finanzaId: Int? = null) {
        viewModelScope.launch {
            categoriesRepository.getCategoriesOptions(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    //ESTO TRAE LA LISTA DE CATEGORIAS PARA EL APARTADO BD CATEGORIAS EGRESO
    fun getCategoriesList(finanzaId: Int? = null) {
        viewModelScope.launch {
            categoriesRepository.getCategoriesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    //TRAE LOS DETALLES DE UNA CATEGORIA EN ESPECIFICO AL DARLE CLICK
    fun getCategoryDetails(categoriaId: Int, finanzaId: Int? = null){
        viewModelScope.launch {
            categoriesRepository.getCategorieData(categoriaId, finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    //CREA CATEGORIA
    fun createCategory(nombreCategoria: String, finanzaId: Int? = null) {
        viewModelScope.launch {
            categoriesRepository.createCategorie(finanzaId, CreateOrUpdateCategorieRequestDomain(nombreCategoria))
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    //ACTUALIZA EL NOMBRE DE LA CATEGORIA, HAGANDO EN EL APARTADO DE DETALLES SI QUIEREN
    //O COMO UN BOTON EN LA LISTA
    fun updateCategory(nombreCategoria: String, categoriaId: Int) {
        viewModelScope.launch {
            categoriesRepository.updateCategorie(categoriaId, CreateOrUpdateCategorieRequestDomain(nombreCategoria))
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                CategoriesViewModel(
                    application.appProvider.provideCategoryRepository()
                )
            }
        }
    }
}