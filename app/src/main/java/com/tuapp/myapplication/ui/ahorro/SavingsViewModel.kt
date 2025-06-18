package com.tuapp.myapplication.ui.ahorro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain
import com.tuapp.myapplication.data.repository.savings.SavingsRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val savingsRepository: SavingsRepository
): ViewModel() {

    fun getSavingsData(anio: Int, finanzaId: Int? = null) {
        viewModelScope.launch {
            savingsRepository.getSavingsData(finanzaId, anio)
                .collect{ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //LISTA DE LOS DATOS DE LOS AHORROS
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    fun createSavingGoal(monto: Double, mes: Int, anio: Int, finanzaId: Int? = null) {
        viewModelScope.launch {
            savingsRepository.createOrUpdateSavings(finanzaId, CreateOrUpdateSavingDomain(monto, mes, anio))
                .collect{ resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //RESPUESTA DE LA CREACION DE UNA META DE AHORRO
                            resource.data.message
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
                SavingsViewModel(
                    application.appProvider.provideSavingRepository(),
                )
            }
        }
    }
}