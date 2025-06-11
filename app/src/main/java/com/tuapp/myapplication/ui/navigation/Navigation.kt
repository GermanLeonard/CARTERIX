package com.tuapp.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tuapp.myapplication.ui.auth.LoginScreen
import com.tuapp.myapplication.ui.auth.RegisterScreen
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.finanzaGrupal.GrupalFinanceScreen
import com.tuapp.myapplication.ui.finanzaIndividual.BDHomeScreen
import com.tuapp.myapplication.ui.finanzaIndividual.CategoriasEgresoScreen
import com.tuapp.myapplication.ui.finanzaIndividual.IndividualFinanceScreen
import com.tuapp.myapplication.ui.finanzaIndividual.IngresosScreen
import com.tuapp.myapplication.ui.finanzaIndividual.RegistrarSubcategoriaScreen
import com.tuapp.myapplication.ui.finanzaIndividual.SubcategoriasScreen
import com.tuapp.myapplication.ui.profile.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {

    val token by userViewModel.token.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if(token != null) FinanzaIndividualScreen else LoginScreen
    ) {

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

