package com.tuapp.myapplication.ui.ingresos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.incomes.request.CreateOrUpdateIncomeRequestDomain
import com.tuapp.myapplication.data.models.incomes.response.IngresoResponseDomain
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

    fun getIncomesList(finanzaId: Int? = null){
        viewModelScope.launch {
            incomesRepository.getIncomesList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //LISTA DE INGRESOS
                            _incomeList.value = resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //DETALLES INGRESO
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
            incomesRepository.createIncome(finanzaId, CreateOrUpdateIncomeRequestDomain(
                nombreIngreso,
                descripcionIngreso,
                montoIngreso)
            )
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //RESPUESTA DE CREACION
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
            incomesRepository.updateIncome(incomeId, CreateOrUpdateIncomeRequestDomain(
                nombreIngreso,
                descripcionIngreso,
                montoIngreso)
            )
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //RESPUESTA DE ACTUALIZACION
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
                IngresosViewModel(
                    application.appProvider.provideIncomesRepository()
                )
            }
        }
    }
}