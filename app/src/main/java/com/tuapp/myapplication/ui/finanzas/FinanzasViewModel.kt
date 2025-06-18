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

    private val _ingresosTotales = MutableStateFlow(0.0)
    val ingresosTotales: StateFlow<Double> = _ingresosTotales.asStateFlow()

    private val _egresosTotales = MutableStateFlow(0.0)
    val egresosTotales: StateFlow<Double> = _egresosTotales.asStateFlow()

    private val _diferencia = MutableStateFlow(0.0)
    val diferencia: StateFlow<Double> = _diferencia.asStateFlow()

    private val _presupuesto = MutableStateFlow(0.0)
    val presupuesto: StateFlow<Double> = _presupuesto.asStateFlow()

    private val _consumo = MutableStateFlow(0.0)
    val consumo: StateFlow<Double> = _consumo.asStateFlow()

    private val _variacion = MutableStateFlow(0.0)
    val variacion: StateFlow<Double> = _variacion.asStateFlow()

    private val _metaMensual = MutableStateFlow<Double?>(null)
    val metaMensual: StateFlow<Double?> = _metaMensual.asStateFlow()

    private val _ahorroMes = MutableStateFlow<Double?>(null)
    val ahorroMes: StateFlow<Double?> = _ahorroMes.asStateFlow()

    private val _ahorroAcumulado = MutableStateFlow<Double?>(null)
    val ahorroAcumulado: StateFlow<Double?> = _ahorroAcumulado.asStateFlow()

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
                            val resumen = resource.data
                            _metaMensual.value = resumen.resumen_ahorros?.meta ?: 0.0
                            _ahorroMes.value = resumen.resumen_ahorros?.progreso_porcentaje ?: 0.0
                            _ahorroAcumulado.value = resumen.resumen_ahorros?.acumulado ?: 0.0

                            _ingresosTotales.value = resumen.resumen_financiero?.ingresos_totales ?: 0.0
                            _egresosTotales.value = resumen.resumen_financiero?.egresos_totales ?: 0.0
                            _diferencia.value = resumen.resumen_financiero?.diferencia ?: 0.0

                            _presupuesto.value = resumen.resumen_egresos?.presupuesto_mensual ?: 0.0
                            _consumo.value = resumen.resumen_egresos?.consumo_mensual ?: 0.0
                            _variacion.value = resumen.resumen_egresos?.variacion_mensual ?: 0.0




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

    fun getFinancesList(){
        viewModelScope.launch {
            finanzaRepository.getFinancesList()
                .collect { resource ->
                    when(resource){
                        is Resource.Loading -> {
                            //Manejen el "cargando"
                        }
                        is Resource.Success -> {
                            //Manejen el "success"
                            //LISTA DE FINANZAS
                            resource.data
                        }
                        is Resource.Error -> {
                            //Manejen el "error"
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
