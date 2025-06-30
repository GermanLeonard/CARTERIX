package com.tuapp.myapplication.ui.ingresos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.Ingreso
import com.tuapp.myapplication.data.models.incomesModels.request.CreateOrUpdateIncomeRequestDomain
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoDetailsResponseDomain
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoResponseDomain
import com.tuapp.myapplication.data.repository.incomes.IncomesRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IngresosViewModel(
    private val incomesRepository: IncomesRepository
): ViewModel() {

    private val _incomeList = MutableStateFlow<List<IngresoResponseDomain>>(emptyList())
    val incomeList: StateFlow<List<IngresoResponseDomain>> = _incomeList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _mensajeError = MutableStateFlow("")
    val mensajeError: StateFlow<String> = _mensajeError

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun getIncomesList(finanzaId: Int? = null, isRefreshing: Boolean = false){
        viewModelScope.launch {
            incomesRepository.getIncomesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            if(isRefreshing){
                                _isRefreshing.value = true
                            } else {
                                _isLoading.value = true
                            }
                            _mensajeError.value = ""
                        }
                        is Resource.Success -> {
                            _incomeList.value = resource.data
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

    private var _ingresoDetails = MutableStateFlow(IngresoDetailsResponseDomain("", 0, 0.0, ""))
    val ingresoDetails: StateFlow<IngresoDetailsResponseDomain> = _ingresoDetails

    fun getIncomeDetails(incomeId: Int){
        viewModelScope.launch {
            incomesRepository.getIncomeDetails(incomeId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            _ingresoDetails.value = resource.data
                        }
                        is Resource.Error -> {
                        }
                    }
                }
        }
    }

    private var _creatingLoading = MutableStateFlow(false)
    val creatingLoading: StateFlow<Boolean> = _creatingLoading

    private var _createdIncome = MutableStateFlow(false)
    val createdIncome: StateFlow<Boolean> = _createdIncome

    private var _creatingError = MutableStateFlow("")
    val creatingError: StateFlow<String> = _creatingError

    fun createIncome(
        nombreIngreso: String,
        descripcionIngreso: String,
        montoIngreso: Double,
        finanzaId: Int? = null,
    ){
        viewModelScope.launch {
            incomesRepository.createIncome(
                finanzaId,
                CreateOrUpdateIncomeRequestDomain(nombreIngreso, descripcionIngreso, montoIngreso)
            ).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        _creatingLoading.value = true
                        _creatingError.value = ""
                    }
                    is Resource.Success -> {
                        _createdIncome.value = resource.data.success
                        _creatingLoading.value = false
                    }
                    is Resource.Error -> {
                        _creatingError.value = resource.message
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private var _updatingLoading = MutableStateFlow(false)
    val updatingLoading: StateFlow<Boolean> = _updatingLoading

    private var _updatedIncome = MutableStateFlow(false)
    val updatedIncome: StateFlow<Boolean> = _updatedIncome

    private var _updatingError = MutableStateFlow("")
    val updatingError: StateFlow<String> = _updatingError

    fun updateIncome(
        nombreIngreso: String,
        descripcionIngreso: String,
        montoIngreso: Double,
        incomeId: Int
    ){
        viewModelScope.launch {
            incomesRepository.updateIncome(
                incomeId,
                CreateOrUpdateIncomeRequestDomain(nombreIngreso, descripcionIngreso, montoIngreso)
            ).collect { resource ->
                when(resource){
                    is Resource.Loading -> {
                        _updatingLoading.value = true
                        _updatingError.value = ""
                    }
                    is Resource.Success -> {
                        _updatedIncome.value = resource.data.success
                        _updatingLoading.value = false
                    }
                    is Resource.Error -> {
                        _updatingError.value = resource.message
                        _updatingLoading.value = false
                    }
                }
            }
        }
    }

    fun resetUpdatedState() {
        _updatedIncome.value = false
    }

    fun resetCreatedState() {
        _createdIncome.value = false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                IngresosViewModel(
                    application.appProvider.provideIncomesRepository()
                )
            }
        }
    }
}