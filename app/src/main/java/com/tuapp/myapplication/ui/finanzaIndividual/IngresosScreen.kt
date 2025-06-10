package com.tuapp.myapplication.ui.finanzaIndividual

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.data.database.AppDatabase
import com.tuapp.myapplication.data.models.Ingreso
import com.tuapp.myapplication.data.repository.IngresoRepository
import com.tuapp.myapplication.ui.viewmodel.IngresoViewModel
import com.tuapp.myapplication.ui.viewmodel.IngresoViewModelFactory
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun IngresosScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).ingresoDao()
    val repo = IngresoRepository(dao)
    val viewModel: IngresoViewModel = viewModel(factory = IngresoViewModelFactory(repo))
    val ingresos by viewModel.ingresos.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    val verde = Color(0xFF2E7D32)

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
                ingresos.forEach {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(it.nombre, fontWeight = FontWeight.Bold)
                            Text("$${it.monto}", color = verde)
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
                            viewModel.agregar(
                                Ingreso(nombre = nombre, monto = monto.toDoubleOrNull() ?: 0.0)
                            )
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
