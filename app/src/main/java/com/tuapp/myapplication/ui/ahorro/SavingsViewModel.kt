package com.tuapp.myapplication.ui.savings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain
import com.tuapp.myapplication.data.models.savingsModels.response.SavingsDataDomain
import com.tuapp.myapplication.data.repository.savings.SavingsRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val savingsRepository: SavingsRepository
): ViewModel() {

    private val _savingsList = MutableStateFlow<List<SavingsDataDomain>>(emptyList())
    val savingsList: StateFlow<List<SavingsDataDomain>> = _savingsList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun getSavingsData(finanzaId: Int?, anio: Int, isRefreshing: Boolean = false, isWebSocketCall: Boolean = false) {
        viewModelScope.launch {
            savingsRepository.getSavingsData(finanzaId, anio).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        if(isRefreshing){
                            _isRefreshing.value = true
                        } else if(!isWebSocketCall) {
                            _isLoading.value = true
                        }
                        _errorMessage.value = ""
                    }
                    is Resource.Success -> {
                        _savingsList.value = resource.data
                        _isLoading.value = false
                        _isRefreshing.value = false
                    }
                    is Resource.Error -> {
                        _errorMessage.value = resource.message
                        _isLoading.value = false
                        _isRefreshing.value = false
                    }
                }
            }
        }
    }

    private var _loadingCreating = MutableStateFlow(false)
    val loadingCreating: StateFlow<Boolean> = _loadingCreating

    private var _createdSavingGoal = MutableStateFlow(false)
    val createdSavingGoal: StateFlow<Boolean> = _createdSavingGoal

    private var _creatingError = MutableStateFlow("")
    val creatingError: StateFlow<String> = _creatingError

    fun createOrUpdateSaving(finanzaId: Int?, data: CreateOrUpdateSavingDomain) {
        viewModelScope.launch {
            savingsRepository.createOrUpdateSavings(finanzaId, data).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _loadingCreating.value = true
                        _creatingError.value = ""
                    }
                    is Resource.Success -> {
                        _createdSavingGoal.value = resource.data.success
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

    fun resetCreatedState() {
        _createdSavingGoal.value = false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                SavingsViewModel(
                    application.appProvider.provideSavingRepository()
                )
            }
        }
    }
}
