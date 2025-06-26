package com.tuapp.myapplication.ui.transacciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun RegistrarTransaccionesScreen(
    navController: NavController,
    finanzaId: Int?,
    transaccionesViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)

    val opcionesTransaccion by transaccionesViewModel.transactionOptions.collectAsStateWithLifecycle()

    //id tipo para guardar en la API
    var idTipo by rememberSaveable {mutableIntStateOf(0)}
    var tipo by remember { mutableStateOf("") }

    //id categoria para guardar en API
    var idCategoria by rememberSaveable { mutableIntStateOf(0) }
    var categoria by remember { mutableStateOf("") }

    var monto by remember { mutableStateOf("") }

    var presupuesto by remember { mutableDoubleStateOf(0.0) }

    var descripcion by remember { mutableStateOf("") }

    var showTipoMenu by remember { mutableStateOf(false) }
    var showCategoriaMenu by remember { mutableStateOf(false) }

    //AGREGUEN UN CAMPO QUE EL USUARIO PUEDA ELEGIR DE FECHA PARA EL REGISTRO,
    // PUES SE TIENE QUE REGISTRAR LA FECHA DE LA TRANSACCION

    LaunchedEffect(Unit) {
        transaccionesViewModel.getTransactionsOptions(finanzaId)
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
                Text("Registrar Transacción", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(top = 32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                    .padding(20.dp)
            ) {
                Text("Tipo")
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6), RoundedCornerShape(8.dp))
                    .clickable { showTipoMenu = true }
                    .padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (tipo.isNotEmpty()) tipo else "Selecciona el tipo")
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(expanded = showTipoMenu, onDismissRequest = { showTipoMenu = false }) {
                    opcionesTransaccion.forEach {
                        DropdownMenuItem(
                            text = { Text(it.tipo_registro_nombre) },
                            onClick = {
                                tipo = it.tipo_registro_nombre
                                idTipo = it.tipo_registro_id
                                showTipoMenu = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Categoría")
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6), RoundedCornerShape(8.dp))
                    .clickable { showCategoriaMenu = true }
                    .padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (categoria.isNotEmpty()) categoria else "Selecciona categoría")
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(expanded = showCategoriaMenu, onDismissRequest = { showCategoriaMenu = false }) {
                    val tipoRegistro = opcionesTransaccion.find { it.tipo_registro_id == idTipo}

                    tipoRegistro?.opciones?.forEach {
                        DropdownMenuItem(
                            text = { Text(it.nombre_opcion) },
                            onClick = {
                                categoria = it.nombre_opcion
                                idCategoria = it.id_opcion
                                presupuesto = it.presupuesto_opcion
                                showCategoriaMenu = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedTextField(
                        value = monto,
                        onValueChange = { monto = it },
                        label = { Text("Monto") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = presupuesto.toString(),
                        onValueChange = {},
                        label = { Text("Presupuesto") },
                        singleLine = true,
                        enabled = false
                    )
                }


                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (idTipo != 0 && idCategoria != 0 && monto.isNotBlank()) {
                            //AQUI TIENEN QUE PASAR SOLO LAS PARAMETROS QUE SE TE PIDAN
                            //transaccionesViewModel.createTransaction(idTipo, idCategoria, monto.toDouble(), descripcion, fechaRegistro, finanzaId )
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = verde),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }
            }
        }

        BottomNavBar(navController = navController, currentRoute = Routes.BD_HOME)
    }
}
