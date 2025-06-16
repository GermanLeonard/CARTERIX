package com.tuapp.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tuapp.myapplication.auth.LoginScreen
import com.tuapp.myapplication.auth.RegisterScreen
import com.tuapp.myapplication.grupales.GrupalFinanceScreen
import com.tuapp.myapplication.individuales.*
import com.tuapp.myapplication.profile.EditProfileScreen
import com.tuapp.myapplication.profile.ProfileScreen
import com.tuapp.myapplication.individuales.DetalleTransaccionScreen


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }

        composable(Routes.INDIVIDUAL) {
            IndividualFinanceScreen(navController)
        }

        composable(Routes.GROUP) {
            GrupalFinanceScreen(navController)
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(navController)
        }

        composable(Routes.BD_HOME) {
            BDHomeScreen(navController)
        }

        composable(Routes.CATEGORIAS_EGRESO) {
            CategoriasEgresoScreen(navController)
        }
        composable(Routes.SUBCATEGORIAS) {
            SubcategoriasScreen(navController)
        }

        composable(Routes.REGISTRAR_SUBCATEGORIA) {
            RegistrarSubcategoriaScreen(navController)
        }

        composable(Routes.INGRESOS) {
            IngresosScreen(navController)
        }

        composable(
            route = Routes.DETALLE_TRANSACCION,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            DetalleTransaccionScreen(navController, transaccionId = id)
        }
        composable(Routes.REGISTRAR_TRANSACCION) {
            RegistrarTransaccionScreen(navController)
        }

        composable(Routes.TRANSACCIONES) {
            TransaccionesScreen(navController)
        }



    }
}



