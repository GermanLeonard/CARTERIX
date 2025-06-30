package com.tuapp.myapplication.ui.subCategorias

import androidx.compose.runtime.MutableState
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
): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _mensajeError = MutableStateFlow("")
    val mensajeError: StateFlow<String> = _mensajeError

    private var _subCategoriesList = MutableStateFlow<List<ListaSubCategoriasDomain>>(emptyList())
    val subCategoriesList: StateFlow<List<ListaSubCategoriasDomain>> = _subCategoriesList

    private var _categoriesExpenses = MutableStateFlow<List<OptionsDomain>>(emptyList())
    val categoriesExpenses: StateFlow<List<OptionsDomain>> = _categoriesExpenses

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun getSubCategoriesList(finanzaId: Int? = null, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            subCategoryRepository.getSubCategoriesList(finanzaId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            if (isRefreshing) {
                                _isRefreshing.value = true
                            } else {
                                _isLoading.value = true
                            }
                            _mensajeError.value = ""
                        }

                        is Resource.Success -> {
                            _subCategoriesList.value = resource.data
                            _isLoading.value = false
                            _isRefreshing.value = false
                        }

                        is Resource.Error -> {
                            _mensajeError.value = resource.message
                            _isLoading.value = false
                            _isRefreshing.value = false
                        }
                    }
                }
        }
    }

    fun getSubCategoryDetails(subCategoryId: Int) {
        viewModelScope.launch {
            subCategoryRepository.getSubCategoryDetails(subCategoryId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            resource.data
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }
    }

    fun getExpensesOptions() {
        viewModelScope.launch {
            subCategoryRepository.getExpensesOptions()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _isLoading.value = true
                        }

                        is Resource.Success -> {
                            _categoriesExpenses.value = resource.data
                            _isLoading.value = false
                        }

                        is Resource.Error -> {
                            _mensajeError.value = resource.message
                            _isLoading.value = false
                        }
                    }
                }
        }
    }

    private var _loadingCreating = MutableStateFlow(false)
    val loadingCreating: StateFlow<Boolean> = _loadingCreating

    private var _createdCategory = MutableStateFlow(false)
    val createdCategory: StateFlow<Boolean> = _createdCategory

    private var _creatingError = MutableStateFlow("")
    val creatingError: StateFlow<String> = _creatingError

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
                when (resource) {
                    is Resource.Loading -> {
                        _loadingCreating.value = true
                        _creatingError.value = ""
                    }

                    is Resource.Success -> {
                        _createdCategory.value = resource.data.success
                        _loadingCreating.value = false
                    }

                    is Resource.Error -> {
                        _creatingError.value = resource.message
                        _loadingCreating.value = false
                    }
                }
            }
        }
    }

    private var _loadingUpdating = MutableStateFlow(false)
    val loadingUpdating: StateFlow<Boolean> = _loadingUpdating

    private var _updatedCategory = MutableStateFlow(false)
    val updatedCategory: StateFlow<Boolean> = _updatedCategory

    private var _updatingError = MutableStateFlow("")
    val updatingError: StateFlow<String> = _updatingError

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
                when (resource) {
                    is Resource.Loading -> {
                        _loadingUpdating.value = true
                        _updatingError.value = ""
                    }

                    is Resource.Success -> {
                        _updatedCategory.value = resource.data.success
                        _loadingUpdating.value = false
                    }

                    is Resource.Error -> {
                        _updatingError.value = resource.message
                        _loadingUpdating.value = false
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