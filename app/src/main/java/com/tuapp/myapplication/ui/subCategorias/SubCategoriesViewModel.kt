package com.tuapp.myapplication.ui.subCategorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.subCategoryModels.request.CreateOrUpdateSubCategoryDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.ListaSubCategoriasDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.OptionsDomain
import com.tuapp.myapplication.data.repository.subCategories.SubCategoryRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubCategoriesViewModel(
    private val subCategoryRepository: SubCategoryRepository
): ViewModel(){

    // Estados para manejar cargando y errores
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError

    private var _subCategoriesList = MutableStateFlow<List<ListaSubCategoriasDomain>>(emptyList())
    val subCategoriesList: StateFlow<List<ListaSubCategoriasDomain>> = _subCategoriesList

    private var _categoriesExpenses = MutableStateFlow<List<OptionsDomain>>(emptyList())
    val categoriesExpenses: StateFlow<List<OptionsDomain>> = _categoriesExpenses

    fun getSubCategoriesList(finanzaId: Int? = null){
        viewModelScope.launch {
            subCategoryRepository.getSubCategoriesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _mensajeError.value = null
                            //LISTA DE SUB CATEGORIAS
                            _subCategoriesList.value = resource.data
                            _isLoading.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _mensajeError.value = resource.message ?: "Error al obtener subcategorias"
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    fun getSubCategoryDetails(subCategoryId: Int) {
        viewModelScope.launch {
            subCategoryRepository.getSubCategoryDetails(subCategoryId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _mensajeError.value = null
                            //DETALLES DE UNA SUB CATEGORIA
                            resource.data
                            _isLoading.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _mensajeError.value = resource.message ?: "Error al obtener detalles"
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    fun getExpensesOptions(){
        viewModelScope.launch {
            subCategoryRepository.getExpensesOptions()
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _mensajeError.value = null
                            //OPCIONES DE LOS TIPOS DE GASTOS PARA LOS SELECT DE LOS FORMULARIOS
                            _categoriesExpenses.value = resource.data
                            _isLoading.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _mensajeError.value = resource.message ?: "Error al obtener tipos de gasto"
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    fun createSubCategory(
        idCategoria: Int,
        nombreSubCategoria: String,
        presupuestoMensual: Double,
        tipoGastoId: Int,
        finanzaId: Int? = null
    ) {
        viewModelScope.launch {
            subCategoryRepository.createSubCategory(
                finanzaId,
                CreateOrUpdateSubCategoryDomain(
                    idCategoria,
                    nombreSubCategoria,
                    presupuestoMensual,
                    tipoGastoId
                )
            ).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        //Manejen el "cargando"
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        //Manejen el "success"
                        _mensajeError.value = null
                        //OPCIONES DE LOS TIPOS DE GASTOS PARA LOS SELECT DE LOS FORMULARIOS
                        resource.data
                        _isLoading.value = false
                    }
                    is Resource.Error -> {
                        //Manejen el "error"
                        _mensajeError.value = resource.message ?: "Error al crear subcategoria"
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun updateSubCategory(
        subCategoriaId: Int,
        idCategoria: Int,
        nombreSubCategoria: String,
        presupuestoMensual: Double,
        tipoGastoId: Int,
    ) {
        viewModelScope.launch {
            subCategoryRepository.updateSubCategory(
                subCategoriaId,
                CreateOrUpdateSubCategoryDomain(
                    idCategoria,
                    nombreSubCategoria,
                    presupuestoMensual,
                    tipoGastoId
                )
            ).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        //Manejen el "cargando"
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        //Manejen el "success"
                        _mensajeError.value = null
                        //OPCIONES DE LOS TIPOS DE GASTOS PARA LOS SELECT DE LOS FORMULARIOS
                        resource.data
                        _isLoading.value = false
                    }
                    is Resource.Error -> {
                        //Manejen el "error"
                        _mensajeError.value = resource.message ?: "Error al actualizar subcategoria"
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                SubCategoriesViewModel(
                    application.appProvider.provideSubCategoryRepository()
                )
            }
        }
    }
}