package com.tuapp.myapplication.ui.consejo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.tuapp.myapplication.CarterixApplication
import com.tuapp.myapplication.data.models.financeModels.response.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenFinancieroResponseDomain
import com.tuapp.myapplication.data.models.incomesModels.response.IngresoResponseDomain
import com.tuapp.myapplication.data.models.savingsModels.response.SavingsDataDomain
import com.tuapp.myapplication.data.models.subCategoryModels.response.ListaSubCategoriasDomain
import com.tuapp.myapplication.data.models.transactionsModels.response.TransactionListResponseDomain
import com.tuapp.myapplication.data.repository.ia.GenerativeRepository
import com.tuapp.myapplication.helpers.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConsejoIAViewModel(
   private val generativeRepository: GenerativeRepository
): ViewModel() {

   private var _loadingAdvice = MutableStateFlow(false)
   val loadingAdvice: StateFlow<Boolean> = _loadingAdvice

   private var _adviceText = MutableStateFlow("")
   val adviceText: StateFlow<String> = _adviceText

   private var _adviceError = MutableStateFlow("")
   val adviceError: StateFlow<String> = _adviceError

   fun generarConsejo(
      resumenFinanza: ResumenFinancieroResponseDomain,
      resumenEgresos: ResumenEgresosResponseDomain,
      listTransacciones: List<TransactionListResponseDomain>,
      listSubCategories: List<ListaSubCategoriasDomain>,
      listaIngresos: List<IngresoResponseDomain>,
      datosAhorro: List<SavingsDataDomain>
   ) {
      viewModelScope.launch {
         val prompt = buildPrompt(
            resumenFinanza, resumenEgresos, listTransacciones, listSubCategories, listaIngresos, datosAhorro
         )
         generativeRepository.generateAdvice(prompt).collect{ resorce ->
            when(resorce) {
               is Resource.Loading -> {
                  _loadingAdvice.value = true
                  _adviceError.value = ""
               }
               is Resource.Success -> {
                  _adviceText.value = resorce.data
                  _loadingAdvice.value = false
               }
               is Resource.Error -> {
                  _adviceError.value = resorce.message
                  _loadingAdvice.value = false
               }
            }
         }
      }
   }

   private fun buildPrompt(
      resumenFinanza: ResumenFinancieroResponseDomain,
      resumenEgresos: ResumenEgresosResponseDomain,
      listTransacciones: List<TransactionListResponseDomain>,
      listSubCategories: List<ListaSubCategoriasDomain>,
      listaIngresos: List<IngresoResponseDomain>,
      datosAhorro: List<SavingsDataDomain>
   ): String {
      return buildString {
         appendLine("Actúa como un consejero financiero profesional.")
         appendLine("Con base en la siguiente información sobre mis finanzas personales,")
         appendLine("proporcióname consejos específicos pero breves para mejorar mi situación financiera.")
         appendLine("Incluye recomendaciones prácticas y un pronóstico estimado de mejora si sigo tus sugerencias.")
         appendLine()

         appendLine("Resumen financiero del mes:")
         appendLine("Total de ingresos: $${resumenFinanza.ingresos_totales}")
         appendLine("Total de gastos: $${resumenFinanza.egresos_totales}")
         appendLine("Diferencia entre ingresos y gastos: $${resumenFinanza.diferencia}")
         appendLine("Presupuesto mensual asignado: $${resumenEgresos.presupuesto_mensual}")
         appendLine()

         appendLine("Últimas transacciones registradas:")
         listTransacciones.take(10).forEach {
            appendLine("- Categoría: ${it.nombre_categoria}, Tipo: ${it.tipo_movimiento_nombre}, Monto: $${it.monto_transaccion}, Fecha: ${it.fecha_transaccion}")
         }
         appendLine()

         appendLine("Subcategorías y presupuestos:")
         listSubCategories.forEach {
            appendLine("- Subcategoría: ${it.sub_categoria_nombre}, Presupuesto: $${it.presupuesto}, Tipo: ${it.tipo_gasto}, Categoría principal: ${it.categoria_nombre}")
         }
         appendLine()

         appendLine("Ingresos registrados:")
         listaIngresos.forEach {
            appendLine("- ${it.nombre_ingreso}: $${it.monto_ingreso}")
         }
         appendLine()

         appendLine("Datos de ahorro:")
         datosAhorro.forEach {
            appendLine("- Mes: ${it.nombre_mes} ${it.anio}, Meta: $${it.meta_ahorro}, Ahorrado: $${it.monto_ahorrado}, Porcentaje de cumplimiento: ${it.porcentaje_cumplimiento}%")
         }
         appendLine()

         appendLine("Por favor, dame consejos concretos para:")
         appendLine("- Ahorrar más dinero")
         appendLine("- Gastar de forma más inteligente")
         appendLine("- Administrar mejor mis ingresos")
         appendLine("- Establecer metas financieras realistas")
         appendLine()

         appendLine("Incluye una predicción estimada de mejora si sigo estos consejos durante los próximos 3 meses.")
         appendLine()
         appendLine("No uses asteriscos (*) para resaltar texto. No formatees el texto con negrita, cursiva ni ningún símbolo de estilo.")
      }
   }


   companion object {
      val Factory: ViewModelProvider.Factory = viewModelFactory {
         initializer {
            val application = this[APPLICATION_KEY] as CarterixApplication
            ConsejoIAViewModel(
               application.appProvider.provideGenerativeRepository()
            )
         }
      }
   }
}