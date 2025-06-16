package com.tuapp.myapplication.navigation

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val INDIVIDUAL = "individual"
    const val GROUP = "grupal"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val BD_HOME = "bd_home"
    const val CATEGORIAS_EGRESO = "categorias_egreso"
    const val SUBCATEGORIAS = "subcategorias"
    const val REGISTRAR_SUBCATEGORIA = "registrar_subcategoria"
    const val INGRESOS = "ingresos"
    const val REGISTRAR_TRANSACCION = "registrar_transaccion"
    const val DETALLE_TRANSACCION = "detalle_transaccion/{id}"
    fun detalleTransaccionRoute(id: Int) = "detalle_transaccion/$id"
    const val TRANSACCIONES = "transacciones"


}
