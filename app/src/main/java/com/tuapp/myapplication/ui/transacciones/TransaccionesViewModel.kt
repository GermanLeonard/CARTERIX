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
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionsOptionsDomain
import com.tuapp.myapplication.data.repository.transactions.TransactionsRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransaccionesViewModel(
    private val transaccionRepository: TransactionsRepository
): ViewModel() {

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var _loadingTransactions = MutableStateFlow(false)
    val loadingTransactions: StateFlow<Boolean> = _loadingTransactions

    private var _transactionsList = MutableStateFlow< List<TransactionListResponseDomain>>(emptyList())
    val transactionsList: StateFlow<List<TransactionListResponseDomain>> = _transactionsList

    private var _transactionDetails = MutableStateFlow(
        TransactionsDetailsDomain("", "", 0.0, "", "", 0.0, "", "", 0)
    )
    val transactionDetails: StateFlow<TransactionsDetailsDomain> = _transactionDetails

    private var _transactionListError = MutableStateFlow("")
    val transactionListError: StateFlow<String> = _transactionListError

    fun getTransactionsList(mes: Int, anio: Int, finanzaId: Int? = null, isRefreshing: Boolean = false, isWebSocketCall: Boolean = false){
        viewModelScope.launch {
            transaccionRepository.getTransactionsList(mes, anio, finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            if(isRefreshing){
                               _isRefreshing.value = true
                            }else if(!isWebSocketCall) {
                                _loadingTransactions.value = true
                            }
                            _transactionListError.value = ""
                        }
                        is Resource.Success -> {
                            _transactionsList.value = resource.data
                            _loadingTransactions.value = false
                            _isRefreshing.value = false
                        }
                        is Resource.Error -> {
                            _transactionListError.value = resource.message
                            _loadingTransactions.value = false
                            _isRefreshing.value = false
                        }
                    }
                }
        }
    }

    private var _transactionOptions = MutableStateFlow<List<TransactionsOptionsDomain>>(emptyList())
    val transactionOptions: StateFlow<List<TransactionsOptionsDomain>> = _transactionOptions

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
                            _transactionOptions.value = resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                        }
                    }
                }
        }
    }

    private var _loadingDetailsTransaction = MutableStateFlow(false)
    val loadingDetailsTransaction: StateFlow<Boolean> = _loadingDetailsTransaction

    private var _detailsErrorMessage = MutableStateFlow("")
    val detailsErrorMessage: StateFlow<String> = _detailsErrorMessage

    fun getTransactionDetails(transaccionId: Int) {
        viewModelScope.launch {
            transaccionRepository.getTransactionDetails(transaccionId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingDetailsTransaction.value = true
                            _detailsErrorMessage.value = ""
                        }
                        is Resource.Success -> {
                            _transactionDetails.value = resource.data
                            _loadingDetailsTransaction.value = false
                        }
                        is Resource.Error -> {
                            _detailsErrorMessage.value = resource.message
                            _loadingDetailsTransaction.value = false
                        }
                    }
                }
        }
    }

    private var _transactionCreated = MutableStateFlow(false)
    val transactionCreated: StateFlow<Boolean> = _transactionCreated

    private var _loadingCreate = MutableStateFlow(false)
    val loadingCreate: StateFlow<Boolean> = _loadingCreate

    private var _createErrorMessage = MutableStateFlow("")
    val createErrorMessage: StateFlow<String> = _createErrorMessage

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
                            _loadingCreate.value = true
                            _transactionCreated.value = false
                        }
                        is Resource.Success -> {
                            _transactionCreated.value = resource.data.success
                            _loadingCreate.value = false
                        }
                        is Resource.Error -> {
                            _createErrorMessage.value = resource.message
                            _loadingCreate.value = false
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