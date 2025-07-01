package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.WebSocketViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.components.MonthSelector
import com.tuapp.myapplication.ui.components.TabSelector
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
import com.tuapp.myapplication.ui.navigation.ConsejoScreen
import com.tuapp.myapplication.ui.navigation.Routes
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IndividualFinanceScreen(
    navController: NavController,
    finanzaViewModel: FinanzasViewModel = viewModel(factory = FinanzasViewModel.Factory),
    finanzaId: Int? = null,
    nombreFinanza: String,
    webSocketViewModel: WebSocketViewModel = viewModel(factory = WebSocketViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)
    val verdePastel = Color(0xFF4CAF50)

    val currentDate = rememberSaveable { LocalDate.now() }

    val currentMonth = rememberSaveable {
        currentDate.month.getDisplayName(TextStyle.FULL, Locale("es")).replaceFirstChar { it.uppercase() }
    }

    var selectedMonth by rememberSaveable { mutableStateOf(currentMonth) }
    var selectedYear by rememberSaveable { mutableIntStateOf(currentDate.year) }

    var selectedTab by rememberSaveable { mutableStateOf("Analisis") }
    var selectedView by rememberSaveable { mutableStateOf("Resumen") }

    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val mes = months.indexOf(selectedMonth) + 1

    val resumenFinanciero by finanzaViewModel.resumenFinanciero.collectAsStateWithLifecycle()
    val resumenEgresos by finanzaViewModel.resumenEgresos.collectAsStateWithLifecycle()
    val resumenAhorros by finanzaViewModel.resumenAhorros.collectAsStateWithLifecycle()
    val loadingResumen by finanzaViewModel.loadingResumen.collectAsStateWithLifecycle()

    val datosFinanza by finanzaViewModel.listaDatosAnalisis.collectAsStateWithLifecycle()
    val loadingDatos by finanzaViewModel.loadingDatos.collectAsStateWithLifecycle()

    val datosErrorMessage by finanzaViewModel.loadingDatosErrorMessage.collectAsStateWithLifecycle()
    val resumenErrorMessage by finanzaViewModel.loadingResumenErrorMessage.collectAsStateWithLifecycle()

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    val resumenFinanzaTrigger by webSocketViewModel.resumenFinanzaTrigger.collectAsStateWithLifecycle()
    val datosFinanzaTrigger by webSocketViewModel.datosFinanzaTrigger.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if(finanzaId != null){
            webSocketViewModel.startListening(finanzaId)
        }
    }

    LaunchedEffect(resumenFinanzaTrigger, selectedMonth) {
        finanzaViewModel.financeSummary(mes, selectedYear, finanzaId, resumenFinanzaTrigger > 0)
    }

    LaunchedEffect(datosFinanzaTrigger, selectedMonth) {
        finanzaViewModel.financeData(mes, selectedYear, finanzaId, datosFinanzaTrigger > 0)
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                nombreFinanza,
                navController,
                showOptions = finanzaId != null,
                finanzaId = finanzaId
            )
        },
        floatingActionButton = {
           FloatingActionButton(
                onClick = {
                    navController.navigate(ConsejoScreen(finanzaId ?: 0))
                },
                containerColor = verde,
                modifier = Modifier
                   .padding(end = 24.dp, bottom = 80.dp)
           ) {
               Icon(Icons.Filled.Check , contentDescription = "Pedir consejo", tint = Color.White )
           }
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        containerColor = verde,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                TabSelector(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    navController = navController,
                    finanzaId = finanzaId ?: 0,
                    nombreFinanza = nombreFinanza
                )
            }
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
                Spacer(modifier = Modifier.height(12.dp))

                MonthSelector(
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear,
                    onMonthSelected = { mesSeleccionado, anioSeleccionado ->
                        selectedMonth = months[mesSeleccionado - 1]
                        selectedYear = anioSeleccionado
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(verdePastel, RoundedCornerShape(50))
                        .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(50))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Resumen", "Datos").forEach { label ->
                        val isSelected = label == selectedView

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(40))
                                .background(
                                    if (isSelected) verdeClaro else Color.Transparent
                                )
                                .clickable { selectedView = label }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if(loadingResumen || loadingDatos){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else if(datosErrorMessage.isNotBlank() || resumenErrorMessage.isNotBlank()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(resumenErrorMessage.ifBlank { datosErrorMessage }, fontSize = 15.sp, color = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedView == "Resumen") {
                    ResumenAnalisisView(
                        resumenFinanciero,
                        resumenEgresos,
                        resumenAhorros
                    )
                }  else {
                    DatosAnalisisView(
                        datosFinanza = datosFinanza,
                        navController = navController
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

}


