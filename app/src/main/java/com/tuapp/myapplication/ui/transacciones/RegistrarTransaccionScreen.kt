package com.tuapp.myapplication.ui.transacciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
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
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
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
    var tipo by rememberSaveable { mutableStateOf("") }

    //id categoria para guardar en API
    var idCategoria by rememberSaveable { mutableIntStateOf(0) }
    var categoria by rememberSaveable { mutableStateOf("") }

    var openDateDialog by rememberSaveable { mutableStateOf(false) }

    var monto by rememberSaveable { mutableStateOf("") }

    var presupuesto by rememberSaveable { mutableDoubleStateOf(0.0) }

    var descripcion by rememberSaveable { mutableStateOf("") }

    var showTipoMenu by rememberSaveable { mutableStateOf(false) }
    var showCategoriaMenu by rememberSaveable { mutableStateOf(false) }

    var fechaTransaccion by rememberSaveable { mutableStateOf("") }

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

                OutlinedTextField(
                    value = fechaTransaccion,
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Fecha Transaccion") },
                    modifier = Modifier.fillMaxWidth().clickable{
                        openDateDialog = true
                    },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar fecha"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))

                if(openDateDialog){
                    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)

                    DatePickerDialog(
                        onDismissRequest = {
                            openDateDialog = false
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDateDialog = false
                                    fechaTransaccion = datePickerState.selectedDateMillis?.let { millis ->
                                        val instant = Instant.ofEpochMilli(millis)
                                        val formatter = DateTimeFormatter.ISO_INSTANT
                                        formatter.format(instant)
                                    } ?: ""
                                }
                            ) {
                                Text("Confirmar fecha")
                            }
                        },
                    ) {
                        DatePicker(datePickerState)
                    }
                }

                Button(
                    onClick = {
                        if (idTipo != 0 && idCategoria != 0 && monto.isNotBlank()) {
                            transaccionesViewModel.createTransaction(idTipo, idCategoria, monto.toDouble(), descripcion, fechaTransaccion, finanzaId )
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
