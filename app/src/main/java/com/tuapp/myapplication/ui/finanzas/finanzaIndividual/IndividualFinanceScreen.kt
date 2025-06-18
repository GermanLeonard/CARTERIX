package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
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
import com.tuapp.myapplication.ui.navigation.BDHomeScreen
import com.tuapp.myapplication.ui.navigation.Routes
import com.tuapp.myapplication.ui.navigation.TransaccionesScreen

@Composable
fun IndividualFinanceScreen(
    navController: NavController,
    finanzaViewModel: FinanzasViewModel = viewModel(factory = FinanzasViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)
    val verdePastel = Color(0xFFE6F4EA)

    var selectedMonth by remember { mutableStateOf("Marzo") }
    var showMonthMenu by remember { mutableStateOf(false) }
    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio")

    var selectedTab by remember { mutableStateOf("Analisis") }
    var selectedView by remember { mutableStateOf("Resumen") }

    LaunchedEffect(Unit) {
        //Aqui tienen que hacer las llamadas segun sea el caso
        //NO QUEMEN LA FECHA, AGARREN LA FECHA ACTUAL AL NOMAS INICIAR LA APLICACION
        //DE AHI CUANDO SE QUIERA FILTRAR SE CAMBIA EL ESTADO DE LA FECHA PARA QUE HAGA EL NUEVO FILTRO
        //TIENE QUE USAR MES Y AÑO
        //EL TERCER PARAMETRO DE LAS FUNCIONES POR SI ACASO HAY FINANZA CONJUNTA, SE PASA EL ID
        //finanzaViewModel.financeData(6,2025)
        //finanzaViewModel.financeSummary(6,2025)
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
                navController = navController
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedMonth, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = month
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = Routes.INDIVIDUAL)
        }
    }
}

