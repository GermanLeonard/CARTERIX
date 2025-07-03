package com.tuapp.myapplication.ui.ingresos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
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

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by incomeViewModel.isRefreshing.collectAsStateWithLifecycle()

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    val loadingCreating by incomeViewModel.creatingLoading.collectAsStateWithLifecycle()
    val createdIncome by incomeViewModel.createdIncome.collectAsStateWithLifecycle()
    val creatingError by incomeViewModel.creatingError.collectAsStateWithLifecycle()

    val loadingUpdating by incomeViewModel.updatingLoading.collectAsStateWithLifecycle()
    val updatedIncome by incomeViewModel.updatedIncome.collectAsStateWithLifecycle()
    val updatingError by incomeViewModel.updatingError.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        incomeViewModel.getIncomesList(finanzaId)
    }

    LaunchedEffect(ingresoDetails) {
        nombre = ingresoDetails.nombre_ingreso
        monto = ingresoDetails.monto_ingreso.toString()
        descripcionIngreso = ingresoDetails.descripcion_ingreso
        selectedIngresoId = ingresoDetails.id_ingreso
    }

    LaunchedEffect(createdIncome) {
        if(createdIncome){
            incomeViewModel.getIncomesList(finanzaId)
            showDialog = false
            incomeViewModel.resetCreatedState()
        }
    }

    LaunchedEffect(updatedIncome) {
        if(updatedIncome){
            incomeViewModel.getIncomesList(finanzaId)
            showEditDialog = false
            incomeViewModel.resetUpdatedState()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.INGRESOS, navController, true)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = verde,
                modifier = Modifier
                    .padding(end = 24.dp, bottom = 80.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
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
                    .padding(horizontal = 25.dp)
            ) {

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else if(mensajeError.isNotBlank()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Text(mensajeError, fontSize = 15.sp, color = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        incomeViewModel.getIncomesList(finanzaId, true)
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(ingresos) { ingreso ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                            ) {
                                Box(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(ingreso.nombre_ingreso, fontWeight = FontWeight.Bold)
                                        Text("$${ingreso.monto_ingreso}", color = verde)
                                    }
                                    IconButton(
                                        onClick = {
                                            incomeViewModel.getIncomeDetails(ingreso.id_ingreso)
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
                                    if (finanzaId != null) {
                                        Text(
                                            ingreso.nombre_usuario,
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            modifier = Modifier.align(Alignment.BottomEnd)
                                        )
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
                        nombre = ""
                        descripcionIngreso = ""
                        monto = ""
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    if(loadingCreating){
                        CircularProgressIndicator()
                    } else {
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
                        Spacer(modifier = Modifier.height(15.dp))
                        if (creatingError.isNotBlank()) {
                            Text(creatingError, fontSize = 15.sp, color = Color.Red)
                        }
                    }
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
                        nombre = ""
                        descripcionIngreso = ""
                        monto = ""
                        selectedIngresoId = 0
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    if(loadingUpdating){
                        CircularProgressIndicator()
                    } else {
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
                        Spacer(modifier = Modifier.height(15.dp))
                        if (updatingError.isNotBlank()) {
                            Text(updatingError, fontSize = 15.sp, color = Color.Red)
                        }
                    }
                }
            }
        )
    }
}
