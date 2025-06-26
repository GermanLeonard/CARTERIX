package com.tuapp.myapplication.ui.ingresos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun IngresosScreen(
    navController: NavController,
    finanzaId: Int?,
    incomeViewModel: IngresosViewModel = viewModel(factory = IngresosViewModel.Factory)
) {
    val ingresos by incomeViewModel.incomeList.collectAsStateWithLifecycle()
    val isLoading by incomeViewModel.isLoading.collectAsStateWithLifecycle()
    val mensajeError by incomeViewModel.mensajeError.collectAsStateWithLifecycle()


    var showDialog by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    val verde = Color(0xFF2E7D32)

    LaunchedEffect(Unit) {
        incomeViewModel.getIncomesList(finanzaId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(verde),
                contentAlignment = Alignment.Center
            ) {
                Text("Ingresos", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .padding(16.dp)
                    .padding(bottom = 70.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.height(16.dp))
                }
                mensajeError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                ingresos.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(it.nombre_ingreso, fontWeight = FontWeight.Bold)
                            Text("$${it.monto_ingreso}", color = verde)
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = verde,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 80.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = Routes.INGRESOS)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        if (nombre.isNotBlank() && monto.isNotBlank()) {
                            nombre = ""
                            monto = ""
                            showDialog = false
                        }
                    }) {
                        Text("Registrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Nuevo Ingreso") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre Ingreso") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = monto,
                            onValueChange = { monto = it },
                            label = { Text("Monto") }
                        )
                    }
                }
            )
        }
    }
}
