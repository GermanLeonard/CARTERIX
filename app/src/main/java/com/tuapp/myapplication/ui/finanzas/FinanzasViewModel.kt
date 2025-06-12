package com.tuapp.myapplication.ui.finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.repository.finance.FinanceRepository
import com.tuapp.myapplication.helpers.Resourse
import kotlinx.coroutines.launch

class FinanzasViewModel(
   private val finanzaRepository: FinanceRepository
): ViewModel() {

    //PONER EL ID DE LA FINANZA EN CASO DE SER CONJUNTA
    fun financeSummary(mes: Int, anio: Int, finanza_id: Int? = null) {
        viewModelScope.launch {
            finanzaRepository.getSummary(mes, anio, finanza_id)
                .collect { resource ->
                    when(resource){
                        is Resourse.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resourse.Success -> {
                            //Manejen el "success"
                            resource.data.finanza_principal
                        }
                        is Resourse.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    //PONER EL ID DE LA FINANZA EN CASO DE SER CONJUNTA
    fun financeData(mes: Int, anio: Int, finanza_id: Int? = null){
        viewModelScope.launch {
            finanzaRepository.getData(mes, anio, finanza_id)
                .collect { resource ->
                    when(resource){
                        is Resourse.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resourse.Success -> {
                            //Manejen el "success"
                            resource.data.categorias
                        }
                        is Resourse.Error -> {
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
                FinanzasViewModel(
                    application.appProvider.provideFinanceRepository()
                )
            }
        }
    }
}