package com.tuapp.myapplication.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.tuapp.myapplication.ui.ahorro.AhorrosScreen
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
import com.tuapp.myapplication.ui.transacciones.DetallesTransaccionScreen
import com.tuapp.myapplication.ui.transacciones.RegistrarTransaccionesScreen
import com.tuapp.myapplication.ui.transacciones.TransaccionesScreen
import com.tuapp.myapplication.ui.finanzas.finanzaGrupal.GroupDetailsScreen
import com.tuapp.myapplication.ui.consejo.ConsejoIAScreen
import com.tuapp.myapplication.ui.finanzas.finanzaIndividual.FiltroGraficoScreen
import com.tuapp.myapplication.ui.profile.EditProfileScreen
import com.tuapp.myapplication.ui.subCategorias.DetalleSubCategoriaScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
    val tokenState by userViewModel.token.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        userViewModel.checkUser()
    }

    when (tokenState) {
        TokenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is TokenState.Loaded -> {

            val token = (tokenState as TokenState.Loaded).token

            NavHost(
                navController = navController,
                startDestination = if (token != null) FinanzaIndividualScreen(
                    0,
                    ""
                ) else LoginScreen
            ) {

                composable<LoginScreen> {
                    LoginScreen(navController)
                }

                composable<RegisterScreen> {
                    RegisterScreen(navController)
                }

                composable<FinanzaIndividualScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val nombreFinanza =
                        backStackEntry.arguments?.getString("nombreFinanza") ?: return@composable

                    val finanzaId: Int? = if (id == 0) null else id
                    val titulo: String =
                        nombreFinanza.ifBlank { "Finanza principal" }

                    IndividualFinanceScreen(
                        navController,
                        finanzaId = finanzaId,
                        nombreFinanza = titulo
                    )
                }

                composable<FinanzaGrupalScreen> {
                    GrupalFinanceScreen(navController = navController)
                }
                composable<PerfilScreen> {
                    ProfileScreen(navController)
                }

                composable<EditProfile> {
                    EditProfileScreen(navController)
                }

                composable<TransaccionesScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId: Int? = if (id == 0) null else id

                    val nombreFinanza =
                        backStackEntry.arguments?.getString("nombreFinanza") ?: return@composable
                    val titulo: String =
                        nombreFinanza.ifBlank { "Finanza principal" }

                    TransaccionesScreen(
                        navController,
                        finanzaId = finanzaId,
                        nombreFinanza = titulo
                    )
                }

                composable<RegistrarTransaccionScreen> { backStackEntry ->

                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId: Int? = if (id == 0) null else id

                    RegistrarTransaccionesScreen(navController, finanzaId)
                }

                composable<DetalleTransaccionScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId =
                        backStackEntry.arguments?.getInt("finanzaId") ?: return@composable
                    DetallesTransaccionScreen(
                        navController,
                        id,
                        if (finanzaId == 0) null else finanzaId
                    )
                }

                composable<BDHomeScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable

                    val nombreFinanza =
                        backStackEntry.arguments?.getString("nombreFinanza") ?: return@composable

                    val titulo: String =
                        nombreFinanza.ifBlank { "Finanza principal" }
                    BDHomeScreen(navController, finanzaId = id, titulo)
                }

                composable<CategoriaEgresoScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId = if (id == 0) null else id
                    CategoriasEgresoScreen(navController, finanzaId)
                }
                composable<SubCategoriaScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId = if (id == 0) null else id
                    SubcategoriasScreen(navController, finanzaId)
                }

                composable<AhorroScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId = if (id == 0) null else id
                    AhorrosScreen(navController, finanzaId)
                }

                composable<RegistrarSubCategoriaScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId = if (id == 0) null else id
                    RegistrarSubcategoriaScreen(navController, finanzaId)
                }

                composable<IngresosScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: return@composable
                    val finanzaId = if (id == 0) null else id
                    IngresosScreen(navController, finanzaId)
                }

                composable<GroupDetailsScreen> { backStackEntry ->
                    val finanzaId =
                        backStackEntry.arguments?.getInt("finanzaId") ?: return@composable
                    GroupDetailsScreen(finanzaId = finanzaId, navController = navController)
                }

                composable<FilterByCategoryScreen> { backStackEntry ->
                    val finanzaId = backStackEntry.arguments?.getInt("finanzaId") ?: return@composable

                    FiltroGraficoScreen(
                        navController = navController,
                        finanzaId = finanzaId,
                    )
                }
                composable<DetalleSubCategoriaRoute> { backStackEntry ->
                    val subcategoriaId = backStackEntry.arguments?.getInt("subcategoriaId") ?: return@composable
                    val id = backStackEntry.arguments?.getInt("finanzaId") ?: return@composable

                    val finanzaId: Int? = if(id != 0) id else null

                    DetalleSubCategoriaScreen(
                        navController = navController,
                        subcategoriaId = subcategoriaId,
                        finanzaId = finanzaId
                    )
                }
                composable<ConsejoScreen> { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("finanzaId") ?: return@composable
                    val finanzaId: Int? = if(id != 0) id else null

                    ConsejoIAScreen(navController, finanzaId)
                }
            }
        }

    }
}