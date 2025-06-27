package com.tuapp.myapplication.ui.ingresos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var monto by rememberSaveable { mutableStateOf("") }
    var descripcionIngreso by rememberSaveable { mutableStateOf("") }

    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var selectedIngresoId by rememberSaveable { mutableIntStateOf(0)}

    val ingresoDetails by incomeViewModel.ingresoDetails.collectAsStateWithLifecycle()

    val verde = Color(0xFF2E7D32)

    LaunchedEffect(Unit) {
        incomeViewModel.getIncomesList(finanzaId)
    }

    LaunchedEffect(ingresoDetails) {
        nombre = ingresoDetails.nombre_ingreso
        monto = ingresoDetails.monto_ingreso.toString()
        descripcionIngreso = ingresoDetails.descripcion_ingreso
        selectedIngresoId = ingresoDetails.id_ingreso
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
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
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(it.nombre_ingreso, fontWeight = FontWeight.Bold)
                                Text("$${it.monto_ingreso}", color = verde)
                            }
                            IconButton(
                                onClick = {
                                    incomeViewModel.getIncomeDetails(it.id_ingreso)
                                    showEditDialog = true
                                },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Color(0xFF3D4B4E)
                                )
                            }
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
                onDismissRequest = {
                    showDialog = false
                    nombre = ""
                    descripcionIngreso = ""
                    monto = ""
                                   },
                confirmButton = {
                    TextButton(onClick = {
                        if (nombre.isNotBlank() && descripcionIngreso.isNotBlank() && monto.isNotBlank()) {
                            incomeViewModel.createIncome(nombre, descripcionIngreso, monto.toDouble(), finanzaId)
                            incomeViewModel.getIncomesList(finanzaId)
                            nombre = ""
                            descripcionIngreso = ""
                            monto = ""
                            showDialog = false
                        }
                    }) {
                        Text("Registrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        nombre = ""
                        descripcionIngreso = ""
                        monto = ""
                        showDialog = false
                    }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Nuevo Ingreso") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre Ingreso") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = descripcionIngreso,
                            onValueChange = { descripcionIngreso = it },
                            label = { Text("Descripcion del ingreso") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = monto,
                            onValueChange = { monto = it },
                            label = { Text("Monto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            )
        } else if (showEditDialog) {
            AlertDialog(
                onDismissRequest = {
                    showEditDialog = false
                    selectedIngresoId = 0
                    nombre = ""
                    descripcionIngreso = ""
                    monto = ""
                                   },
                confirmButton = {
                    TextButton(onClick = {
                        if (nombre.isNotBlank() && descripcionIngreso.isNotBlank() && monto.isNotBlank()) {
                            incomeViewModel.updateIncome(nombre, descripcionIngreso, monto.toDouble(), selectedIngresoId)
                            incomeViewModel.getIncomesList(finanzaId)
                            nombre = ""
                            descripcionIngreso = ""
                            monto = ""
                            selectedIngresoId = 0
                            showEditDialog = false
                        }
                    }) {
                        Text("Editar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        nombre = ""
                        descripcionIngreso = ""
                        monto = ""
                        selectedIngresoId = 0
                        showEditDialog = false
                    }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Editar Ingreso") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre Ingreso") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = descripcionIngreso,
                            onValueChange = { descripcionIngreso = it },
                            label = { Text("Descripcion del ingreso") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = monto,
                            onValueChange = { monto = it },
                            label = { Text("Monto") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun DialogoCargando(showDialog: Boolean, ){
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* No se puede cancelar */ },
            confirmButton = {},
            title = { Text("Cargando...") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        )
    }
}

