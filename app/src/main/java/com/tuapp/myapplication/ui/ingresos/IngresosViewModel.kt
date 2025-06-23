package com.tuapp.myapplication.ui.ingresos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.incomesModels.request.CreateOrUpdateIncomeRequestDomain
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

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError

    fun getIncomesList(finanzaId: Int? = null){
        viewModelScope.launch {
            incomesRepository.getIncomesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _isLoading.value = false
                            _mensajeError.value = null
                            //LISTA DE INGRESOS
                            _incomeList.value = resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _isLoading.value = false
                            _mensajeError.value = resource.message ?: "Error al obtener ingresos"
                        }
                    }
                }
        }
    }

    fun getIncomeDetails(incomeId: Int){
        viewModelScope.launch {
            incomesRepository.getIncomeDetails(incomeId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                            _isLoading.value = true
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _isLoading.value = false
                            _mensajeError.value = null
                            //DETALLES INGRESO
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _isLoading.value = false
                            _mensajeError.value = resource.message ?: "Error al obtener detalles del ingreso"
                        }
                    }
                }
        }
    }

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
                        //Manejen el "cargando"
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        //Manejen el "success"
                        _isLoading.value = false
                        _mensajeError.value = null
                        //RESPUESTA DE CREACION
                        resource.data
                    }
                    is Resource.Error -> {
                        //Manejen el "error"
                        _isLoading.value = false
                        _mensajeError.value = resource.message ?: "Error al crear ingreso"
                    }
                }
            }
        }
    }

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
                        //Manejen el "cargando"
                        _isLoading.value = true
                    }
                    is Resource.Success -> {
                        //Manejen el "success"
                        _isLoading.value = false
                        _mensajeError.value = null
                        //RESPUESTA DE ACTUALIZACION
                        resource.data
                    }
                    is Resource.Error -> {
                        //Manejen el "error"
                        _isLoading.value = false
                        _mensajeError.value = resource.message ?: "Error al actualizar ingreso"
                    }
                }
            }
        }
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