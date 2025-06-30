package com.tuapp.myapplication.ui.ingresos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.RegistrarTransaccionScreen
import com.tuapp.myapplication.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngresosScreen(
    navController: NavController,
    finanzaId: Int?,
    ingresosViewModel: IngresosViewModel = viewModel(factory = IngresosViewModel.Factory)
) {
    val ingresosList by ingresosViewModel.incomeList.collectAsStateWithLifecycle()
    val isLoading by ingresosViewModel.isLoading.collectAsStateWithLifecycle()
    val mensajeError by ingresosViewModel.mensajeError.collectAsStateWithLifecycle()
    val isRefreshing by ingresosViewModel.isRefreshing.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        ingresosViewModel.getIncomesList(finanzaId)
    }

    val pullToRefreshState = rememberPullToRefreshState()
    val currentRoute = if (finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL
    val verde = Color(0xFF2E7D32)

    Scaffold(
        topBar = {
            CustomTopBar(Routes.INGRESOS, navController, true)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(RegistrarTransaccionScreen(finanzaId ?: 0)) },
                containerColor = verde,
                modifier = Modifier.padding(end = 24.dp, bottom = 100.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
            }
        },
        containerColor = verde,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
                    .padding(horizontal = 25.dp)
            ) {
                Spacer(modifier = Modifier.height(25.dp))

                if (mensajeError != null) {
                    Text(
                        text = mensajeError ?: "",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        ingresosViewModel.getIncomesList(finanzaId, true)
                    }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(ingresosList) { ingreso ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                ingreso.nombre_ingreso,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text("Monto: $${ingreso.monto_ingreso}")
                                        }
                                        if (finanzaId != null) {
                                            Text(
                                                ingreso.nombre_usuario,
                                                color = Color.Gray,
                                                fontSize = 12.sp,
                                                modifier = Modifier.align(Alignment.Bottom)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
