package com.tuapp.myapplication.ui.finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.repository.finance.FinanceRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.launch

class FinanzasViewModel(
   private val finanzaRepository: FinanceRepository
): ViewModel() {

    //CREEN USTEDES LOS ESTADOS QUE SERAN NECESARIOS A MOSTRAR EN LA VISTA
    // (la informacion, estados de cargando, mensajes de error, etc.)

    //PONER EL ID DE LA FINANZA EN CASO DE SER CONJUNTA
    //ESTO ES LO QUE SE MOSTRARA EN LA PANTALLA PRINCIPAL
    //ESTO SE MOSTRARA EN LA PANTALLA DE LA FINANZA EN EL APARTADO "ANALISIS" "RESUMEN"
    fun financeSummary(mes: Int, anio: Int, finanza_id: Int? = null) {
        viewModelScope.launch {
            finanzaRepository.getSummary(mes, anio, finanza_id)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            resource.data.resumen_ahorros
                            resource.data.resumen_financiero
                            resource.data.resumen_egresos
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    //PONER EL ID DE LA FINANZA EN CASO DE SER CONJUNTA
    //ESTO SE MOSTRARA EN LA PANTALLA DE LA FINANZA EN EL APARTADO "ANALISIS" "DATOS"
    fun financeData(mes: Int, anio: Int, finanza_id: Int? = null){
        viewModelScope.launch {
            finanzaRepository.getData(mes, anio, finanza_id)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
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
                FinanzasViewModel(
                    application.appProvider.provideFinanceRepository()
                )
            }
        }
    }
}