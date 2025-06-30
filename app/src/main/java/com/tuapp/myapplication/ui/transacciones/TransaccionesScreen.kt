// reemplaza la vista anterior
package com.tuapp.myapplication.ui.transacciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.components.MonthSelector
import com.tuapp.myapplication.ui.components.TabSelector
import com.tuapp.myapplication.ui.navigation.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaccionesScreen(
    navController: NavController,
    transaccionViewModel: TransaccionesViewModel = viewModel(factory = TransaccionesViewModel.Factory),
    finanzaId: Int?,
    nombreFinanza: String
) {

    val currentDate = rememberSaveable { LocalDate.now() }

    val currentMonth = rememberSaveable {
        currentDate.month.getDisplayName(TextStyle.FULL, Locale("es")).replaceFirstChar { it.uppercase() }
    }

    var selectedMonth by rememberSaveable { mutableStateOf(currentMonth) }
    var selectedYear by rememberSaveable { mutableIntStateOf(currentDate.year) }

    val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val mes = months.indexOf(selectedMonth) + 1

    val loadingTransactions by transaccionViewModel.loadingTransactions.collectAsStateWithLifecycle()
    val transactions by transaccionViewModel.transactionsList.collectAsStateWithLifecycle()

    val verde = Color(0xFF2E7D32)
    var selectedTab by rememberSaveable { mutableStateOf("Transacciones") }

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by transaccionViewModel.isRefreshing.collectAsStateWithLifecycle()

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    //AGREGUEN OPCION PARA FILTRAR POR MES Y AÑO
    LaunchedEffect(selectedMonth) {
        transaccionViewModel.getTransactionsList(mes, selectedYear, finanzaId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(RegistrarTransaccionScreen(finanzaId ?: 0)) },
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
        topBar = {
            CustomTopBar(nombreFinanza, navController)
        },
        containerColor = verde,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ){
                TabSelector(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    navController = navController,
                    finanzaId = finanzaId ?: 0,
                    nombreFinanza = nombreFinanza
                )
            }

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
                    Spacer(modifier = Modifier.height(12.dp))

                    MonthSelector(
                        selectedMonth = selectedMonth,
                        selectedYear = selectedYear,
                        onMonthSelected = { mesSeleccionado, anioSeleccionado ->
                            selectedMonth = months[mesSeleccionado - 1]
                            selectedYear = anioSeleccionado
                        }
                    )

                    if(loadingTransactions){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            CircularProgressIndicator()
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PullToRefreshBox(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            transaccionViewModel.getTransactionsList(mes, selectedYear, finanzaId, true)
                        }
                    ) {
                        if (transactions.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No hay transacciones registradas.", color = Color.Gray)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(transactions) { t ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp)
                                            .clickable {
                                                navController.navigate(DetalleTransaccionScreen(t.transaccion_id, finanzaId ?: 0))
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text("${t.tipo_movimiento_nombre} - ${t.nombre_categoria}", fontWeight = FontWeight.Bold)
                                            Text(
                                                "$${t.monto_transaccion}",
                                                color = if (t.tipo_movimiento_nombre == "Ingreso") verde else Color.Red
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
}
