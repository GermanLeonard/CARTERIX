package com.tuapp.myapplication.ui.transacciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun DetallesTransaccionScreen(
    navController: NavController,
    transaccionId: Int,
    transaccionesViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)

    val loadingDetails by transaccionesViewModel.loadingDetailsTransaction.collectAsStateWithLifecycle()
    val transaccion by transaccionesViewModel.transactionDetails.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        transaccionesViewModel.getTransactionDetails(transaccionId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(verde),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Detalle de Transacción", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(top = 32.dp))
            }

            if(loadingDetails){
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }

            transaccion.let { t ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                        .padding(24.dp)
                ) {
                    Text("Tipo:", fontWeight = FontWeight.Bold)
                    Text(t.tipo_movimiento, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Categoría:", fontWeight = FontWeight.Bold)
                    Text(t.categoria, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Monto:", fontWeight = FontWeight.Bold)
                    Text("$${t.monto}", fontSize = 18.sp, color = if (t.tipo_movimiento == "Ingreso") verde else Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Descripción:", fontWeight = FontWeight.Bold)
                    Text(t.descripcion_gasto, fontSize = 18.sp)
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = Routes.INDIVIDUAL)
        }
    }
}
