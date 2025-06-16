package com.tuapp.myapplication.ui.transacciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tuapp.myapplication.components.BottomNavBar
import com.tuapp.myapplication.data.database.AppDatabase
import com.tuapp.myapplication.data.repository.TransaccionRepository
import com.tuapp.myapplication.data.viewmodel.TransaccionViewModel
import com.tuapp.myapplication.data.viewmodel.TransaccionViewModelFactory

@Composable
fun DetalleTransaccionScreen(navController: NavController, transaccionId: Int) {
    val verde = Color(0xFF2E7D32)
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).transaccionDao()
    val repo = TransaccionRepository(dao)
    val viewModel: TransaccionViewModel = viewModel(factory = TransaccionViewModelFactory(repo))

    val transaccion by viewModel.getById(transaccionId).collectAsState(initial = null)

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

            transaccion?.let { t ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                        .padding(24.dp)
                ) {
                    Text("Tipo:", fontWeight = FontWeight.Bold)
                    Text(t.tipo, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Categoría:", fontWeight = FontWeight.Bold)
                    Text(t.categoria, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Monto:", fontWeight = FontWeight.Bold)
                    Text("$${t.monto}", fontSize = 18.sp, color = if (t.tipo == "Ingreso") verde else Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Descripción:", fontWeight = FontWeight.Bold)
                    Text(t.descripcion, fontSize = 18.sp)
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cargando...", color = Color.Gray)
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = Routes.INDIVIDUAL)
        }
    }
}
