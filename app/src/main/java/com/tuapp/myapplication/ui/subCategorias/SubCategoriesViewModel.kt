package com.tuapp.myapplication.ui.subCategorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.subCategoryModels.request.CreateOrUpdateSubCategoryDomain
import com.tuapp.myapplication.data.repository.subCategories.SubCategoryRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.launch

class SubCategoriesViewModel(
    private val subCategoryRepository: SubCategoryRepository
): ViewModel(){

    fun getSubCategoriesList(finanzaId: Int? = null){
        viewModelScope.launch {
            subCategoryRepository.getSubCategoriesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //LISTA DE SUB CATEGORIAS
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //DETALLES DE UNA SUB CATEGORIA
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //OPCIONES DE LOS TIPOS DE GASTOS PARA LOS SELECT DE LOS FORMULARIOS
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
                    }
                    is Resource.Success -> {
                        //Manejen el "success"
                        //OPCIONES DE LOS TIPOS DE GASTOS PARA LOS SELECT DE LOS FORMULARIOS
                        resource.data
                    }
                    is Resource.Error -> {
                        //Manejen el "error"
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
                    }
                    is Resource.Success -> {
                        //Manejen el "success"
                        //OPCIONES DE LOS TIPOS DE GASTOS PARA LOS SELECT DE LOS FORMULARIOS
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
                SubCategoriesViewModel(
                    application.appProvider.provideSubCategoryRepository()
                )
            }
        }
    }
}