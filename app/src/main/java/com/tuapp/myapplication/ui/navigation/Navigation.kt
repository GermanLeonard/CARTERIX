package com.tuapp.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tuapp.myapplication.ui.auth.LoginScreen
import com.tuapp.myapplication.ui.auth.RegisterScreen
import com.tuapp.myapplication.ui.finanzaGrupal.GrupalFinanceScreen
import com.tuapp.myapplication.ui.finanzaIndividual.BDHomeScreen
import com.tuapp.myapplication.ui.finanzaIndividual.CategoriasEgresoScreen
import com.tuapp.myapplication.ui.finanzaIndividual.IndividualFinanceScreen
import com.tuapp.myapplication.ui.finanzaIndividual.IngresosScreen
import com.tuapp.myapplication.ui.finanzaIndividual.RegistrarSubcategoriaScreen
import com.tuapp.myapplication.ui.finanzaIndividual.SubcategoriasScreen
import com.tuapp.myapplication.ui.profile.ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = LoginScreen) {

        composable <LoginScreen> {
            LoginScreen(navController)
        }

        composable<RegisterScreen> {
            RegisterScreen(navController)
        }

        composable<FinanzaIndividualScreen> {
            IndividualFinanceScreen(navController)
        }

        composable<FinanzaGrupalScreen> {
            GrupalFinanceScreen(navController)
        }

        composable<PerfilScreen> {
            ProfileScreen(navController)
        }

        composable<BDHomeScreen> {
            BDHomeScreen(navController)
        }

        composable<CategoriaEgresoScreen> {
            CategoriasEgresoScreen(navController)
        }
        composable<SubCategoriaScreen> {
            SubcategoriasScreen(navController)
        }

        composable<RegistrarSubCategoriaScreen> {
            RegistrarSubcategoriaScreen(navController)
        }

        composable<IngresosScreen> {
            IngresosScreen(navController)
        }
    }
}

