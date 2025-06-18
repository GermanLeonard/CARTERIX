package com.tuapp.myapplication.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tuapp.myapplication.helpers.TokenState
import com.tuapp.myapplication.profile.ProfileScreen
import com.tuapp.myapplication.ui.auth.LoginScreen
import com.tuapp.myapplication.ui.auth.RegisterScreen
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.finanzas.finanzaGrupal.GrupalFinanceScreen
import com.tuapp.myapplication.ui.finanzas.finanzaIndividual.BDHomeScreen
import com.tuapp.myapplication.ui.categorias.CategoriasEgresoScreen
import com.tuapp.myapplication.ui.finanzas.finanzaIndividual.IndividualFinanceScreen
import com.tuapp.myapplication.ui.ingresos.IngresosScreen
import com.tuapp.myapplication.ui.subCategorias.RegistrarSubcategoriaScreen
import com.tuapp.myapplication.ui.subCategorias.SubcategoriasScreen
import com.tuapp.myapplication.ui.transacciones.DetalleTransaccionScreen
import com.tuapp.myapplication.ui.transacciones.RegistrarTransaccionScreen
import com.tuapp.myapplication.ui.transacciones.TransaccionesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {

    val tokenState by userViewModel.token.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        userViewModel.checkUser()
    }

    when(tokenState){
        TokenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TokenState.Loaded -> {

            val token = "12345"//(tokenState as TokenState.Loaded).token LA CAMBIE SOLO PARA PODER ACCEDER A LAS VISTAS CUANDO SE HAGA EL MERGE SE CAMBIA

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

                composable<TransaccionesScreen> {
                    TransaccionesScreen(navController)
                }

                composable<RegistrarTransaccionScreen> {
                    RegistrarTransaccionScreen(navController)
                }

                composable<DetalleTransaccionScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    DetalleTransaccionScreen(id)
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
    }

}

