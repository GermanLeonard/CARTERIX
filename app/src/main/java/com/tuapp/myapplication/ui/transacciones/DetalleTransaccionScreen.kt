package com.tuapp.myapplication.ui.transacciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun DetallesTransaccionScreen(
    navController: NavController,
    transaccionId: Int,
    finanzaId: Int?,
    transaccionesViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)

    val loadingDetails by transaccionesViewModel.loadingDetailsTransaction.collectAsStateWithLifecycle()
    val transaccion by transaccionesViewModel.transactionDetails.collectAsStateWithLifecycle()

    var tipoMovimiento by rememberSaveable { mutableStateOf("") }
    var movimiento by rememberSaveable { mutableStateOf("") }
    var nombreCategoria by rememberSaveable { mutableStateOf("") }
    var tipoGasto by rememberSaveable { mutableStateOf("") }
    var presupuesto by rememberSaveable { mutableDoubleStateOf(0.0) }
    var monto by rememberSaveable { mutableDoubleStateOf(0.0) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var nombreRegistro by rememberSaveable { mutableStateOf("") }

    val detailsErrorMessage by transaccionesViewModel.detailsErrorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        transaccionesViewModel.getTransactionDetails(transaccionId)
    }

    LaunchedEffect(transaccion) {
        tipoMovimiento = transaccion.tipo_movimiento
        movimiento = transaccion.movimiento
        nombreCategoria = transaccion.categoria
        tipoGasto = transaccion.tipo_gasto
        presupuesto = transaccion.presupuesto
        monto = transaccion.monto
        descripcion = transaccion.descripcion_gasto
        nombreRegistro = transaccion.nombre_usuario
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.DETALLE_TRANSACCION, navController, true)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Routes.INDIVIDUAL)
        },
        containerColor = verde,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                if(loadingDetails){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else if(detailsErrorMessage.isNotBlank()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(detailsErrorMessage, fontSize = 15.sp, color = Color.Red)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.White,
                            RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                        )
                        .padding(24.dp),
                ) {
                    Text("Tipo", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = tipoMovimiento,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Movimiento", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = movimiento,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Categoría", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = nombreCategoria,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tipo de Gasto", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = tipoGasto,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        Text("Presupuesto", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = "$$presupuesto",
                            onValueChange = {},
                            singleLine = true,
                            enabled = false,
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Monto", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = "$$monto",
                            onValueChange = {},
                            singleLine = true,
                            enabled = false,
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Descripción del gasto", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if(finanzaId != null){
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Registrado por", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = nombreRegistro,
                            onValueChange = {},
                            singleLine = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                }
            }
        }
    }
}
