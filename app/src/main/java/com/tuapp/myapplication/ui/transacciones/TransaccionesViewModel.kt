package com.tuapp.myapplication.ui.transacciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.transactionsModels.request.CreateTransactionDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionListResponseDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsDetailsDomain
import com.tuapp.myapplication.data.repository.transactions.TransactionsRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransaccionesViewModel(
    private val transaccionRepository: TransactionsRepository
): ViewModel() {

    private var _transactionsList = MutableStateFlow< List<TransactionListResponseDomain>>(emptyList())
    val transactionsList: StateFlow<List<TransactionListResponseDomain>> = _transactionsList

    private var _transactionDetails = MutableStateFlow<TransactionsDetailsDomain>(
        TransactionsDetailsDomain("", "", 0.0, "", "", 0.0, "", "", 0)
    )
    val transactionDetails: StateFlow<TransactionsDetailsDomain> = _transactionDetails

    fun getTransactionsList(finanzaId: Int? = null){
        viewModelScope.launch {
            transaccionRepository.getTransactionsList(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //LISTA DE TRANSACCIONES
                            _transactionsList.value = resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    fun getTransactionsOptions(finanzaId: Int? = null){
        viewModelScope.launch {
            transaccionRepository.getTransactionOptions(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //OPCIONES PARA LA CREACION DE TRANSACCIONES
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    fun getTransactionDetails(transaccionId: Int) {
        viewModelScope.launch {
            transaccionRepository.getTransactionDetails(transaccionId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //DETALLES DE UNA TRANSACCION
                            _transactionDetails.value = resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    fun createTransaction(
        tipoTransaccion: Int,
        tipoMovimieto: Int,
        monto: Double,
        descripcion: String,
        fechaRegistro: String,
        finanzaId: Int? = null
    ) {
        viewModelScope.launch {
            transaccionRepository.createTransaction(
                CreateTransactionDomain(descripcion,fechaRegistro,monto,tipoMovimieto,tipoTransaccion),
                finanzaId
            )
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //RESPUESTA DE LA CREACION DE UNA TRANSACCION
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
                TransaccionesViewModel(
                    application.appProvider.provideTransactionRepository()
                )
            }
        }
    }
}