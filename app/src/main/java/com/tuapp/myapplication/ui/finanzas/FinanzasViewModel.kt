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
import com.tuapp.myapplication.data.models.financeModels.response.DatoAnalisisDomain
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenFinancieroResponseDomain
import com.tuapp.myapplication.data.repository.finance.FinanceRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinanzasViewModel(
    private val finanzaRepository: FinanceRepository
): ViewModel() {

    private var _roleId = MutableStateFlow(0)
    val roleId: StateFlow<Int> = _roleId

    // NUEVOS ESTADOS PARA RESUMEN ANALISIS

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


    fun getRole(finanzaId: Int) {
        viewModelScope.launch {
            finanzaRepository.getRole(finanzaId).collect { role ->
                _roleId.value = role
            }
        }
    }

    //CREEN USTEDES LOS ESTADOS QUE SERAN NECESARIOS A MOSTRAR EN LA VISTA
    // (la informacion, estados de cargando, mensajes de error, etc.)

    //PONER EL ID DE LA FINANZA EN CASO DE SER CONJUNTA
    //ESTO ES LO QUE SE MOSTRARA EN LA PANTALLA PRINCIPAL
    //ESTO SE MOSTRARA EN LA PANTALLA DE LA FINANZA EN EL APARTADO "ANALISIS" "RESUMEN"

    private var _loadingResumen = MutableStateFlow(false)
    val loadingResumen: StateFlow<Boolean> = _loadingResumen

    private var _loadingResumenErrorMessage = MutableStateFlow("")
    val loadingResumenErrorMessage: StateFlow<String> = _loadingResumenErrorMessage

    fun financeSummary(mes: Int, anio: Int, finanza_id: Int? = null) {
        viewModelScope.launch {
            finanzaRepository.getSummary(mes, anio, finanza_id)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            _loadingResumen.value = true
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

    fun financeData(mes: Int, anio: Int, finanza_id: Int? = null){
        viewModelScope.launch {
            finanzaRepository.getData(mes, anio, finanza_id)
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            // manejar cargando si querés
                            _loadingDatos.value = true
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

    fun createInvite(finanzaId: Int){
        viewModelScope.launch {
            finanzaRepository.createInvite(finanzaId)
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

    fun getFinancesList() {
        viewModelScope.launch {
            finanzaRepository.getFinancesList()
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            // Opcional: Puedes manejar estado de carga si deseas
                        }
                        is Resource.Success -> {
                            _listaGrupos.value = resource.data
                        }
                        is Resource.Error -> {
                            // Opcional: Mostrar error o log
                        }
                    }
                }
        }
    }

    fun getFinanceDetails(finanzaId: Int){
        viewModelScope.launch {
            finanzaRepository.getFinanceDetails(finanzaId)
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

    fun joinFinance(codigo: String){
        viewModelScope.launch {
            finanzaRepository.joinFinance(JoinFinanceRequestDomain(codigo))
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

    fun createFinance(titulo: String, descripcion: String) {
        viewModelScope.launch {
            finanzaRepository.createFinance(CreateFinanceRequestDomain(titulo, descripcion))
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

    fun leaveFinance(finanzaId: Int) {
        viewModelScope.launch {
            finanzaRepository.leaveFinance(finanzaId)
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

    fun deleteFromFinance(userId: Int, finanzaId: Int) {
        viewModelScope.launch {
            finanzaRepository.deleteFromFinance(userId, finanzaId)
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
