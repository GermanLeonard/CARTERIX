// reemplaza la vista anterior
package com.tuapp.myapplication.ui.transacciones

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tuapp.myapplication.ui.navigation.DetalleTransaccionScreen
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun TransaccionesScreen(
    navController: NavController,
    transaccionViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModel.Factory)
    ) {

    val transacciones by transaccionViewModel.transactionsList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        transaccionViewModel.getTransactionsList()
    }

    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)
    val verdePastel = Color(0xFFE6F4EA)
    val currentRoute = Routes.INDIVIDUAL

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(verde),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Finanza principal", fontSize = 24.sp, color = Color.White, modifier = Modifier.padding(top = 32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(verdePastel, RoundedCornerShape(50))
                        .border(BorderStroke(2.dp, verde), RoundedCornerShape(50))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Analisis", "Transacciones", "BD").forEach { tab ->
                        val isSelected = tab == "Transacciones"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp)
                                .background(
                                    if (isSelected) verdeClaro else Color.Transparent,
                                    RoundedCornerShape(50)
                                )
                                .clickable {
                                    when (tab) {
                                        "Analisis" -> navController.navigate(Routes.INDIVIDUAL)
                                        "BD" -> navController.navigate(Routes.BD_HOME)
                                    }
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tab, color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (transacciones.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay transacciones registradas.", color = Color.Gray)
                    }
                } else {
                    LazyColumn {
                        items(transacciones) { t ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        navController.navigate(DetalleTransaccionScreen(t.transaccion_id))

                                    },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("${t.tipo_movimiento_id} - ${t.nombre_categoria}", fontWeight = FontWeight.Bold)
                                    Text("$${t.monto_transaccion}", color = if (t.tipo_movimiento_nombre == "Ingreso") verde else Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Routes.REGISTRAR_TRANSACCION) },
            containerColor = verde,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 80.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}
