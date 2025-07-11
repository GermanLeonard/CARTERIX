package com.tuapp.myapplication.ui.finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.financeModels.request.CreateFinanceRequestDomain
import com.tuapp.myapplication.data.models.financeModels.request.JoinFinanceRequestDomain
import com.tuapp.myapplication.data.models.financeModels.response.CategorieResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinanceDetailsResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenFinancieroResponseDomain
import com.tuapp.myapplication.data.repository.finance.FinanceRepository
import com.tuapp.myapplication.data.repository.ia.GenerativeRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FinanzasViewModel(
    private val finanzaRepository: FinanceRepository,
    private val generativeRepository: GenerativeRepository
): ViewModel() {

    private var _resumenFinanciero = MutableStateFlow(ResumenFinancieroResponseDomain(0.0, 0.0, 0.0))
    val resumenFinanciero: StateFlow<ResumenFinancieroResponseDomain> = _resumenFinanciero

    private var _resumenEgresos = MutableStateFlow(ResumenEgresosResponseDomain(0.0,0.0,0.0))
    val resumenEgresos: StateFlow<ResumenEgresosResponseDomain> = _resumenEgresos

    private var _resumenAhorros = MutableStateFlow(ResumenAhorrosResponseDomain(0.0, 0.0, 0.0))
    val resumenAhorros = _resumenAhorros

    private val _listaDatosAnalisis = MutableStateFlow<List<CategorieResponseDomain>>(emptyList())
    val listaDatosAnalisis: StateFlow<List<CategorieResponseDomain>> = _listaDatosAnalisis

    private val _listaGrupos = MutableStateFlow<List<FinancesListResponseDomain>>(emptyList())
    val listaGrupos: StateFlow<List<FinancesListResponseDomain>> = _listaGrupos

    private var _loadingResumen = MutableStateFlow(false)
    val loadingResumen: StateFlow<Boolean> = _loadingResumen

    private var _loadingResumenErrorMessage = MutableStateFlow("")
    val loadingResumenErrorMessage: StateFlow<String> = _loadingResumenErrorMessage

    fun financeSummary(mes: Int, anio: Int, finanzaId: Int? = null, isWebSocketCall: Boolean = false) {
        viewModelScope.launch {
            finanzaRepository.getSummary(mes, anio, finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            if(!isWebSocketCall){
                                _loadingResumen.value = true
                            }
                            _loadingResumenErrorMessage.value = ""
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            _resumenFinanciero.value = resource.data.resumen_financiero
                            _resumenEgresos.value = resource.data.resumen_egresos
                            _resumenAhorros.value = resource.data.resumen_ahorros
                            _loadingResumen.value = false
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
                            _loadingResumen.value = false
                            _loadingResumenErrorMessage.value = resource.message
                        }
                    }
                }
        }
    }

    //PONER EL ID DE LA FINANZA EN CASO DE SER CONJUNTA
    //ESTO SE MOSTRARA EN LA PANTALLA DE LA FINANZA EN EL APARTADO "ANALISIS" "DATOS"

    private var _loadingDatos = MutableStateFlow(false)
    val loadingDatos: StateFlow<Boolean> = _loadingDatos

    private var _loadingDatosErrorMessage = MutableStateFlow("")
    val loadingDatosErrorMessage: StateFlow<String> = _loadingDatosErrorMessage

    fun financeData(mes: Int, anio: Int, finanzaId: Int? = null, isWebSocketCall: Boolean = false){
        viewModelScope.launch {
            finanzaRepository.getData(mes, anio, finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            if(!isWebSocketCall){
                                _loadingDatos.value = true
                            }
                            _loadingDatosErrorMessage.value = ""
                        }
                        is Resource.Success -> {
                            _listaDatosAnalisis.value = resource.data
                            _loadingDatos.value = false
                        }
                        is Resource.Error -> {
                            // mostrar error si querés
                            _loadingDatos.value = false
                            _loadingDatosErrorMessage.value = resource.message
                        }
                    }
                }
        }
    }

    private var _loadingInvite = MutableStateFlow(false)
    val loadingInvite: StateFlow<Boolean> = _loadingInvite

    private var _inviteError = MutableStateFlow("")
    val inviteError: StateFlow<String> = _inviteError

    private var _inviteCode = MutableStateFlow("")
    val inviteCode: StateFlow<String> = _inviteCode

    fun createInvite(finanzaId: Int){
        viewModelScope.launch {
            finanzaRepository.createInvite(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingInvite.value = true
                            _inviteError.value = ""
                        }
                        is Resource.Success -> {
                            _inviteCode.value = resource.data.codigo_invitacion
                            _loadingInvite.value = false
                        }
                        is Resource.Error -> {
                            _inviteError.value = resource.message
                            _loadingInvite.value = false
                        }
                    }
                }
        }
    }

    fun resetCode() {
        _inviteCode.value = ""
    }

    private var _isRefreshingFinanceList = MutableStateFlow(false)
    val isRefreshingFinanceList: StateFlow<Boolean> = _isRefreshingFinanceList

    private var _loadingFinancesList = MutableStateFlow(false)
    val loadingFinanceList: StateFlow<Boolean> = _loadingFinancesList

    private var _loadingFinancesListError = MutableStateFlow("")
    val loadingFinancesListError: StateFlow<String> = _loadingFinancesListError

    fun getFinancesList(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            finanzaRepository.getFinancesList()
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            if(isRefreshing){
                                _isRefreshingFinanceList.value = true
                            } else {
                                _loadingFinancesList.value = true
                            }
                            _loadingFinancesListError.value = ""
                        }
                        is Resource.Success -> {
                            _listaGrupos.value = resource.data
                            _loadingFinancesList.value = false
                            _isRefreshingFinanceList.value = false
                        }
                        is Resource.Error -> {
                            _loadingFinancesListError.value = resource.message
                            _loadingFinancesList.value = false
                            _isRefreshingFinanceList.value = false
                        }
                    }
                }
        }
    }

    private var _financeDetails = MutableStateFlow(
        FinanceDetailsResponseDomain(
       "", emptyList(), ""
    ))
    val financeDetails: StateFlow<FinanceDetailsResponseDomain> = _financeDetails

    private var _loadingDetails = MutableStateFlow(false)
    val loadingDetails: StateFlow<Boolean> = _loadingDetails

    private var _detailsError = MutableStateFlow("")
    val detailsError: StateFlow<String> = _detailsError

    fun getFinanceDetails(finanzaId: Int){
        viewModelScope.launch {
            finanzaRepository.getFinanceDetails(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingDetails.value = true
                            _detailsError.value = ""
                        }
                        is Resource.Success -> {
                            _financeDetails.value = resource.data
                            _loadingDetails.value = false
                        }
                        is Resource.Error -> {
                            _detailsError.value = resource.message
                            _loadingDetails.value = false
                        }
                    }
                }
        }
    }

    private var _loadingJoin = MutableStateFlow(false)
    val loadingJoin: StateFlow<Boolean> = _loadingJoin

    private var _joinedFinance = MutableStateFlow(false)
    val joinedFinance: StateFlow<Boolean> = _joinedFinance

    private var _joiningError = MutableStateFlow("")
    val joiningError: StateFlow<String> = _joiningError

    fun joinFinance(codigo: String){
        viewModelScope.launch {
            finanzaRepository.joinFinance(JoinFinanceRequestDomain(codigo))
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingJoin.value = true
                            _joiningError.value = ""
                        }
                        is Resource.Success -> {
                            _joinedFinance.value = resource.data.success
                            _loadingJoin.value = false
                        }
                        is Resource.Error -> {
                            _joiningError.value = resource.message
                            _loadingJoin.value = false
                        }
                    }
                }
        }
    }

    fun resetJoinedState() {
        _joinedFinance.value = false
    }

    private var _loadingCreate = MutableStateFlow(false)
    val loadingCreate: StateFlow<Boolean> = _loadingCreate

    private var _createdFinance = MutableStateFlow(false)
    val createdFinance: StateFlow<Boolean> = _createdFinance

    private var _creatingError = MutableStateFlow("")
    val creatingError: StateFlow<String> = _creatingError

    fun createFinance(titulo: String, descripcion: String) {
        viewModelScope.launch {
            finanzaRepository.createFinance(CreateFinanceRequestDomain(titulo, descripcion))
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingCreate.value = true
                            _creatingError.value = ""
                        }
                        is Resource.Success -> {
                            _createdFinance.value = resource.data.success
                            _loadingCreate.value = false
                        }
                        is Resource.Error -> {
                            _creatingError.value = resource.message
                            _loadingCreate.value = false
                        }
                    }
                }
        }
    }

    fun resetCreatedState() {
        _createdFinance.value = false
    }

    private var _loadingLeave = MutableStateFlow(false)
    val loadingLeave: StateFlow<Boolean> = _loadingLeave

    private var _leavedUser = MutableStateFlow(false)
    val leavedUser: StateFlow<Boolean> = _leavedUser

    private var _leavingError = MutableStateFlow("")
    val leavingError: StateFlow<String> = _leavingError

    fun leaveFinance(finanzaId: Int) {
        viewModelScope.launch {
            finanzaRepository.leaveFinance(finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingLeave.value = true
                            _leavingError.value = ""
                        }
                        is Resource.Success -> {
                            _leavedUser.value = resource.data.success
                            _loadingLeave.value = false
                        }
                        is Resource.Error -> {
                            _leavingError.value = resource.message
                            _loadingLeave.value = false
                        }
                    }
                }
        }
    }

    fun resetLeavedState() {
        _leavedUser.value = false
    }

    private var _loadingDelete = MutableStateFlow(false)
    val loadingDelete: StateFlow<Boolean> = _loadingDelete

    private var _deletedUser = MutableStateFlow(false)
    val deletedUser: StateFlow<Boolean> = _deletedUser

    private var _deletingError = MutableStateFlow("")
    val deletingError: StateFlow<String> = _deletingError

    fun deleteFromFinance(userId: Int, finanzaId: Int) {
        viewModelScope.launch {
            finanzaRepository.deleteFromFinance(userId, finanzaId)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingDelete.value = true
                            _deletingError.value = ""
                        }
                        is Resource.Success -> {
                            _deletedUser.value = resource.data.success
                            _loadingDelete.value = false
                        }
                        is Resource.Error -> {
                            _deletingError.value = ""
                            _loadingDelete.value = false
                        }
                    }
                }
        }
    }

    fun resetDeletedUserState() {
        _deletedUser.value = false
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as CarterixApplication
                FinanzasViewModel(
                    application.appProvider.provideFinanceRepository(),
                    application.appProvider.provideGenerativeRepository()
                )
            }
        }
    }
}
