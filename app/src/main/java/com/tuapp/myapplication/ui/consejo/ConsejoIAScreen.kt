package com.tuapp.myapplication.ui.consejo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
import com.tuapp.myapplication.ui.ingresos.IngresosViewModel
import com.tuapp.myapplication.ui.navigation.Routes
import com.tuapp.myapplication.ui.savings.SavingsViewModel
import com.tuapp.myapplication.ui.subCategorias.SubCategoriesViewModel
import com.tuapp.myapplication.ui.transacciones.TransaccionesViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConsejoIAScreen(
    navController: NavController,
    finanzaId: Int?,
    finanzasViewModel: FinanzasViewModel = viewModel(factory = FinanzasViewModel.Factory),
    transaccionesViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModel.Factory),
    subCategoriasViewModel: SubCategoriesViewModel = viewModel(factory = SubCategoriesViewModel.Factory),
    ingresosViewModel: IngresosViewModel = viewModel(factory = IngresosViewModel.Factory),
    ahorroViewModel: SavingsViewModel = viewModel(factory = SavingsViewModel.Factory),
    consejoIAViewModel: ConsejoIAViewModel = viewModel(factory = ConsejoIAViewModel.Factory)
) {

    val verde = Color(0xFF2E7D32)
    val currentDate = rememberSaveable { LocalDate.now() }

    val currentMonth = rememberSaveable {
        currentDate.monthValue
    }
    val currentYear = rememberSaveable {
        currentDate.year
    }

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    val loadingFinanzaResumen by finanzasViewModel.loadingResumen.collectAsStateWithLifecycle()
    val loadingTransacciones by transaccionesViewModel.loadingTransactions.collectAsStateWithLifecycle()
    val loadingSubCategorias by subCategoriasViewModel.isLoading.collectAsStateWithLifecycle()
    val loadingIngresos by ingresosViewModel.isLoading.collectAsStateWithLifecycle()
    val loadingAhorro by ahorroViewModel.isLoading.collectAsStateWithLifecycle()

    val resumenFinanza by finanzasViewModel.resumenFinanciero.collectAsStateWithLifecycle()
    val resumenEgresos by finanzasViewModel.resumenEgresos.collectAsStateWithLifecycle()
    val listaTransacciones by transaccionesViewModel.transactionsList.collectAsStateWithLifecycle()
    val subCategoriasList by subCategoriasViewModel.subCategoriesList.collectAsStateWithLifecycle()
    val ingresosList by ingresosViewModel.incomeList.collectAsStateWithLifecycle()
    val savingsList by ahorroViewModel.savingsList.collectAsStateWithLifecycle()

    val loadingAdvice by consejoIAViewModel.loadingAdvice.collectAsStateWithLifecycle()
    val advice by consejoIAViewModel.adviceText.collectAsStateWithLifecycle()
    val adviceError by consejoIAViewModel.adviceError.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        finanzasViewModel.financeSummary(currentMonth, currentYear, finanzaId)
        transaccionesViewModel.getTransactionsList(currentMonth, currentYear, finanzaId)
        ingresosViewModel.getIncomesList(finanzaId)
        subCategoriasViewModel.getSubCategoriesList(finanzaId)
        ahorroViewModel.getSavingsData(finanzaId, currentYear)
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.CONSEJO, navController = navController, true )
        },
        bottomBar = {
            BottomNavBar(navController, currentRoute)
        },
        contentColor = Color.Black,
        containerColor = verde
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {

                if(loadingFinanzaResumen || loadingTransacciones || loadingIngresos || loadingAhorro || loadingSubCategorias) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize().verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(25.dp))

                        Button(
                            onClick = {
                                consejoIAViewModel.generarConsejo(
                                    resumenFinanza = resumenFinanza,
                                    resumenEgresos = resumenEgresos,
                                    listTransacciones = listaTransacciones,
                                    listSubCategories = subCategoriasList,
                                    listaIngresos = ingresosList,
                                    datosAhorro = savingsList,
                                )
                            },
                            enabled = !(loadingFinanzaResumen || loadingTransacciones || loadingIngresos || loadingAhorro || loadingSubCategorias || loadingAdvice),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = verde),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = "Pedir consejo",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        Box(
                            modifier = Modifier.fillMaxSize().padding(top = 16.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            when {
                                loadingAdvice -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator()
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Generando consejo...", fontSize = 15.sp)
                                    }
                                }

                                adviceError.isNotBlank() -> {
                                    Text(adviceError, fontSize = 15.sp, color = Color.Red)
                                }

                                advice.isNotBlank() -> {
                                    Text(advice, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}