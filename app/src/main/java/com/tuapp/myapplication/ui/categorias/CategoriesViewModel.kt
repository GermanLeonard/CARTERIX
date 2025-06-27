package com.tuapp.myapplication.ui.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.categoryModels.request.CreateOrUpdateCategorieRequestDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesListDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesOptionsDomain
import com.tuapp.myapplication.data.repository.categories.CategoryRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val categoriesRepository: CategoryRepository
): ViewModel() {

    private var _categoriesList = MutableStateFlow<List<CategoriesListDomain>>(emptyList())
    val categoriesList: StateFlow<List<CategoriesListDomain>> = _categoriesList

    private var _categoriesOptions = MutableStateFlow<List<CategoriesOptionsDomain>>(emptyList())
    val categoriesOptions: StateFlow<List<CategoriesOptionsDomain>> = _categoriesOptions

    private var _loadingCategories = MutableStateFlow(false)
    val loadingCategories: StateFlow<Boolean> = _loadingCategories
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
                            //LISTA DE LAS OPCIONES DE CATEGORIAS PARA LOS SELECTS DE LOS FORMULARIOS
                            _categoriesOptions.value = resource.data
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
                            _loadingCategories.value = true
                        }
                        is Resource.Success -> {
                            _categoriesList.value = resource.data
                            _loadingCategories.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _loadingCategories.value = false
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
                            //DETALLES DEL FILTRADO POR CATEGORIAS
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
                            _loadingCategories.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //RESPUESTA DE LA CREACION DE LA CATEGORIA
                            resource.data
                            _loadingCategories.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _loadingCategories.value = false
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
                            _loadingCategories.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //RESPUESTA DE LA ACTUALIZACION DE LA CATEGORIA
                            resource.data
                            _loadingCategories.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _loadingCategories.value = false
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