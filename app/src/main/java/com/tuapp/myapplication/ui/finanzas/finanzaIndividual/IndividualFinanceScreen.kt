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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
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
    finanzaViewModel: FinanzasViewModel = viewModel(factory = FinanzasViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)
    val verdePastel = Color(0xFFE6F4EA)

    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember {
        currentDate.month.getDisplayName(TextStyle.FULL, Locale("es")).replaceFirstChar { it.uppercase() }
    }
    val currentYear = remember { currentDate.year }

    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    var showMonthMenu by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Analisis") }
    var selectedView by remember { mutableStateOf("Resumen") }

    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")

    val ahorroMes by finanzaViewModel.ahorroMes.collectAsState()
    val ahorroAcumulado by finanzaViewModel.ahorroAcumulado.collectAsState()
    val metaMensual by finanzaViewModel.metaMensual.collectAsState()

    val ingresos by finanzaViewModel.ingresosTotales.collectAsState()
    val egresos by finanzaViewModel.egresosTotales.collectAsState()
    val diferencia by finanzaViewModel.diferencia.collectAsState()
    val presupuesto by finanzaViewModel.presupuesto.collectAsState()
    val consumo by finanzaViewModel.consumo.collectAsState()
    val variacion by finanzaViewModel.variacion.collectAsState()

    LaunchedEffect(Unit) {
        val mes = months.indexOf(currentMonth) + 1
        finanzaViewModel.financeSummary(mes, currentYear)
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
                onTabSelected = {
                    selectedTab = it
                    if (it == "Transacciones") {
                        navController.navigate(Routes.TRANSACCIONES)
                    } else if (it == "BD") {
                        navController.navigate(Routes.BD_HOME)
                    }
                },
                navController = navController
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$selectedMonth $selectedYear", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    IconButton(onClick = { showMonthMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Seleccionar mes",
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = showMonthMenu,
                        onDismissRequest = { showMonthMenu = false }
                    ) {
                        months.forEachIndexed { index, month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = month
                                    val mes = index + 1
                                    selectedYear = LocalDate.now().year
                                    finanzaViewModel.financeSummary(mes, selectedYear)
                                    showMonthMenu = false
                                }
                            )
                        }
                    }
                }
            }

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

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedView == "Resumen") {
                ResumenAnalisisView(
                    ahorroMes = ahorroMes ?: 0.0,
                    ahorroAcumulado = ahorroAcumulado ?: 0.0,
                    metaMensual = metaMensual ?: 0.0,
                    ingresos = ingresos,
                    egresos = egresos,
                    diferencia = diferencia,
                    presupuesto = presupuesto,
                    consumo = consumo,
                    variacion = variacion
                )
            } else {
                Text("Vista de Datos aún no implementada.")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = Routes.INDIVIDUAL)
        }
    }
}


