package com.tuapp.myapplication.ui.ahorro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.navigation.NavHostController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.Routes
import com.tuapp.myapplication.ui.savings.SavingsViewModel
import com.tuapp.myapplication.ui.savings.components.NuevaMetaDialog
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AhorrosScreen(
    navController: NavHostController,
    finanzaId: Int?,
    viewModel: SavingsViewModel = viewModel(factory = SavingsViewModel.Factory)
) {

    val verde = Color(0xFF2E7D32)

    val ahorroList by viewModel.savingsList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.errorMessage.collectAsStateWithLifecycle()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val anio = calendar.get(Calendar.YEAR)

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    LaunchedEffect(Unit) {
        viewModel.getSavingsData(finanzaId, anio)
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.AHORRO, navController, true)
        },
        bottomBar = { BottomNavBar(navController = navController, currentRoute = currentRoute) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF2E7D32)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Registrar Ahorro")
            }
        },
        containerColor = verde,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                    Column {
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        PullToRefreshBox(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                viewModel.getSavingsData(finanzaId, anio, true)
                            }
                        ) {
                            if (ahorroList.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        "No hay metas registradas",
                                    )
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(ahorroList) { ahorro ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(
                                                    0xFFF5F5F5
                                                )
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(
                                                    text = ahorro.nombre_mes,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Spacer(modifier = Modifier.height(6.dp))

                                                RowItem(
                                                    "Ahorro Del Mes",
                                                    ahorro.monto_ahorrado.takeIf { it > 0 }
                                                        ?.let { "$ %.2f".format(it) } ?: "$ 0.00")
                                                RowItem(
                                                    "Ahorro Acum.",
                                                    "$ %.2f".format(ahorro.monto_ahorrado)
                                                )
                                                RowItem(
                                                    "Meta Mensual",
                                                    "$ %.2f".format(ahorro.meta_ahorro)
                                                )
                                                RowItem(
                                                    "Cumplimiento",
                                                    "%.0f%%".format(ahorro.porcentaje_cumplimiento),
                                                    color = Color(0xFF388E3C)
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
            NuevaMetaDialog(
                onDismiss = { showDialog = false },
                onCreate = { data ->
                    viewModel.createOrUpdateSaving(finanzaId, data)
                    viewModel.getSavingsData(finanzaId, anio)
                    showDialog = false
                }
            )
        }
    }
}


@Composable
fun RowItem(label: String, value: String, color: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Blue, fontSize = 14.sp)
        Text(text = value, color = color, fontSize = 14.sp)
    }
}
