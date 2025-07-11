package com.tuapp.myapplication.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
data class FinanzaIndividualScreen(val id: Int, val nombreFinanza: String)

@Serializable
object FinanzaGrupalScreen

@Serializable
object PerfilScreen

@Serializable
object EditProfile

@Serializable
data class BDHomeScreen(val id: Int, val nombreFinanza: String)

@Serializable
data class CategoriaEgresoScreen(val id: Int)

@Serializable
data class SubCategoriaScreen(val id: Int)

@Serializable
data class RegistrarSubCategoriaScreen(val id: Int)

@Serializable
data class IngresosScreen(val id: Int)

@Serializable
data class RegistrarTransaccionScreen(val id: Int)

@Serializable
data class DetalleTransaccionScreen(val id: Int, val finanzaId: Int)

@Serializable
data class TransaccionesScreen(val id: Int, val nombreFinanza: String)

@Serializable
data class AhorroScreen(val id: Int)

@Serializable
data class GroupDetailsScreen(val finanzaId: Int)

@Serializable
data class FilterByCategoryScreen(val finanzaId: Int, val nombreFinanza: String)

@Serializable
data class DetalleSubCategoriaRoute(val subcategoriaId: Int, val finanzaId: Int)

@Serializable
data class ConsejoScreen(val finanzaId: Int)

object Routes {
    const val INDIVIDUAL = "individual"
    const val GROUP = "grupal"
    const val PROFILE = "Perfil"
    const val EDIT_PROFILE = "Editar Perfil"
    const val BD_HOME = "bd_home"
    const val CATEGORIAS_EGRESO = "Categorias de Egreso"
    const val SUBCATEGORIAS = "Sub Categorias de Egreso"
    const val REGISTRAR_SUBCATEGORIA = "Registrar Sub Categoria"
    const val INGRESOS = "Ingresos"
    const val REGISTRAR_TRANSACCION = "Registrar Transaccion"
    const val DETALLE_TRANSACCION = "Detalles Transaccion"
    const val DETALLE_FINANZA = "Detalles Finanza"
    const val FINANZA_CONJUNTA = "Lista de Finanzas"
    const val FILTRAR_CATEGORIA = "Filtrar Categoria"
    const val AHORRO = "Metas de Ahorro"
    const val CONSEJO = "Pidele un tip a la IA"
}