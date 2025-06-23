package com.tuapp.myapplication.ui.savings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.navigation.Routes
import com.tuapp.myapplication.ui.savings.components.NuevaMetaDialog
import java.util.*

@Composable
fun AhorroScreen(
    navController: NavHostController,
    viewModel: SavingsViewModel
) {
    val ahorroList by viewModel.savingsList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val mes = calendar.get(Calendar.MONTH) + 1
    val anio = calendar.get(Calendar.YEAR)
    val finanzaId: Int? = null

    LaunchedEffect(true) {
        viewModel.getSavingsData(finanzaId, anio)
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, currentRoute = Routes.BD_HOME) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF2E7D32)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Registrar Ahorro")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Encabezado verde con flecha y título
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFF2E7D32)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Ahorro",
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
                    }
                }

                // Contenedor blanco redondeado
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Meta de ahorro",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (isLoading) {
                            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                        } else if (ahorroList.isEmpty()) {
                            Text("No hay metas registradas", Modifier.align(Alignment.CenterHorizontally))
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(ahorroList) { ahorro ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = ahorro.nombre_mes,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))

                                            RowItem("Ahorro Del Mes", ahorro.monto_ahorrado.takeIf { it > 0 }?.let { "$ %.2f".format(it) } ?: "$ -")
                                            RowItem("Ahorro Acum.", "$ %.2f".format(ahorro.monto_ahorrado))
                                            RowItem("Meta Mensual", "$ %.2f".format(ahorro.meta_ahorro))
                                            RowItem("Cumplimiento", "%.0f%%".format(ahorro.porcentaje_cumplimiento), color = Color(0xFF388E3C))
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

    if (showDialog) {
        NuevaMetaDialog(
            onDismiss = { showDialog = false },
            onCreate = { data ->
                viewModel.createOrUpdateSaving(finanzaId, data)
                viewModel.getSavingsData(finanzaId, anio)
                showDialog = false
            }
        )
    }
}

@Composable
fun RowItem(label: String, value: String, color: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Blue, fontSize = 14.sp)
        Text(text = value, color = color, fontSize = 14.sp)
    }
}
