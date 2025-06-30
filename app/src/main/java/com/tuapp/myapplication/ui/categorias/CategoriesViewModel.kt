package com.tuapp.myapplication.ui.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.categoryModels.request.CreateOrUpdateCategorieRequestDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategorieDataResponseDomain
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

    private var _categoryDetails = MutableStateFlow<CategorieDataResponseDomain?>(null)
    val categoryDetails: StateFlow<CategorieDataResponseDomain?> = _categoryDetails

    private var _loadingDetails = MutableStateFlow(false)
    val loadingDetails: StateFlow<Boolean> = _loadingDetails

    private var _loadingOptions = MutableStateFlow(false)
    val loadingOptions: StateFlow<Boolean> = _loadingOptions

    fun getCategoriesOptions(finanzaId: Int? = null) {
        viewModelScope.launch {
            categoriesRepository.getCategoriesOptions(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingOptions.value = true
                        }
                        is Resource.Success -> {
                            _categoriesOptions.value = resource.data
                            _loadingOptions.value = false
                        }
                        is Resource.Error -> {
                            _loadingOptions.value = false
                        }
                    }
                }
        }
    }

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var _loadingCategoriesError = MutableStateFlow("")
    val loadingCategoriesError: StateFlow<String> = _loadingCategoriesError

    fun getCategoriesList(finanzaId: Int? = null, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            categoriesRepository.getCategoriesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            if(isRefreshing){
                                _isRefreshing.value = true
                            } else {
                                _loadingCategories.value = true
                            }
                            _loadingCategoriesError.value = ""
                        }
                        is Resource.Success -> {
                            _categoriesList.value = resource.data
                            _loadingCategories.value = false
                            _isRefreshing.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _loadingCategoriesError.value = resource.message
                            _loadingCategories.value = false
                            _isRefreshing.value = false
                        }
                    }
                }
        }
    }

    fun getCategoryDetails(categoriaId: Int, finanzaId: Int? = null){
        viewModelScope.launch {
            categoriesRepository.getCategorieData(categoriaId, finanzaId)
                .collect { resource ->
                    when(resource) {
                        is Resource.Loading -> {
                            _loadingDetails.value = true
                        }
                        is Resource.Success -> {
                            _categoryDetails.value = resource.data
                            _loadingDetails.value = false
                        }
                        is Resource.Error -> {
                            _loadingDetails.value = false
                            // Manejar error
                        }
                    }
                }
        }
    }

    private var _loadingCreating = MutableStateFlow(false)
    val loadingCreating: StateFlow<Boolean> = _loadingCreating

    private var _creatingCategory = MutableStateFlow(false)
    val creatingCategory: StateFlow<Boolean> = _creatingCategory

    private var _creatingError = MutableStateFlow("")
    val creatingError: StateFlow<String> = _creatingError

    fun createCategory(nombreCategoria: String, finanzaId: Int? = null) {
        viewModelScope.launch {
            categoriesRepository.createCategorie(finanzaId, CreateOrUpdateCategorieRequestDomain(nombreCategoria))
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingCreating.value = true
                            _creatingError.value = ""
                        }
                        is Resource.Success -> {
                            _creatingCategory.value = resource.data.success
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
    val loadingUpdate: StateFlow<Boolean> = _loadingUpdating

    private var _updatedCategory = MutableStateFlow(false)
    val updatedCategory: StateFlow<Boolean> = _updatedCategory

    private var _updatingError = MutableStateFlow("")
    val updatingError: StateFlow<String> = _updatingError

    //ACTUALIZA EL NOMBRE DE LA CATEGORIA, HAGANDO EN EL APARTADO DE DETALLES SI QUIEREN
    //O COMO UN BOTON EN LA LISTA
    fun updateCategory(nombreCategoria: String, categoriaId: Int) {
        viewModelScope.launch {
            categoriesRepository.updateCategorie(categoriaId, CreateOrUpdateCategorieRequestDomain(nombreCategoria))
                .collect { resource ->
                    when(resource){
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

    fun resetUpdateState(){
        _updatedCategory.value = false
    }

    fun resetCreatingState(){
        _creatingCategory.value = false
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