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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.MonthSelector
import com.tuapp.myapplication.ui.components.TabSelector
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
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
) {
    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)
    val verdePastel = Color(0xFFE6F4EA)

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


    LaunchedEffect(selectedMonth) {
        finanzaViewModel.financeSummary(mes, selectedYear, finanzaId)
        finanzaViewModel.financeData(mes, selectedYear, finanzaId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(verde),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                "Finanza principal",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 24.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            TabSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                navController = navController,
                finanzaId = finanzaId ?: 0,
            )

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
                Modifier
                    .fillMaxWidth()
                    .background(verdePastel, RoundedCornerShape(50))
                    .border(BorderStroke(2.dp, verde), RoundedCornerShape(50))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Resumen", "Datos").forEach { label ->
                    val isSelected = label == selectedView
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .background(
                                if (isSelected) verdeClaro else Color.Transparent,
                                RoundedCornerShape(50)
                            )
                            .clickable { selectedView = label }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, color = Color.Black, fontWeight = FontWeight.Medium)
                    }
                }
            }

            if(loadingResumen || loadingDatos){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
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
                    DatosAnalisisView(datosFinanza)
                }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL)
        }
    }
}


