package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain
import com.tuapp.myapplication.ui.auth.UserViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
import com.tuapp.myapplication.ui.navigation.FinanzaIndividualScreen
import com.tuapp.myapplication.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrupalFinanceScreen(
    navController: NavHostController,
    viewModel: FinanzasViewModel = viewModel(factory = FinanzasViewModel.Factory),
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)

    val userCredentials by userViewModel.userCredential.collectAsStateWithLifecycle()
    var nombre by rememberSaveable { mutableStateOf("") }

    val loadingFinanceList by viewModel.loadingFinanceList.collectAsStateWithLifecycle()
    val loadingFinanceListError by viewModel.loadingFinancesListError.collectAsStateWithLifecycle()

    val grupos by viewModel.listaGrupos.collectAsState()
    var showJoinDialog by rememberSaveable { mutableStateOf(false) }
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var showLeaveFinanceDialog by rememberSaveable { mutableStateOf(false) }

    val createdFinance by viewModel.createdFinance.collectAsStateWithLifecycle()
    val creatingError by viewModel.creatingError.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by viewModel.isRefreshingFinanceList.collectAsStateWithLifecycle()

    val joinedFinance by viewModel.joinedFinance.collectAsStateWithLifecycle()
    val joiningError by viewModel.joiningError.collectAsStateWithLifecycle()
    val leavedFinance by viewModel.leavedUser.collectAsStateWithLifecycle()
    var selectedFinance by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(userCredentials) {
        nombre = userCredentials?.nombre ?: ""
    }

    LaunchedEffect(Unit) {
        viewModel.getFinancesList()
    }

    LaunchedEffect(createdFinance) {
        if(createdFinance){
            viewModel.getFinancesList()
            showCreateDialog = false
            viewModel.resetCreatedState()
        }
    }

    LaunchedEffect(joinedFinance) {
        if(joinedFinance){
            viewModel.getFinancesList()
            showJoinDialog = false
            viewModel.resetJoinedState()
        }
    }

    LaunchedEffect(leavedFinance) {
        if(leavedFinance){
            viewModel.getFinancesList()
            selectedFinance = 0
            showLeaveFinanceDialog = false
            viewModel.resetLeavedState()
        }
    }

    fun onClick() {
        showLeaveFinanceDialog = true
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.FINANZA_CONJUNTA, navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF2E7D32)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear grupo")
            }
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Routes.GROUP)
        },
        contentColor = Color.Black,
        containerColor = verde
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                    if(loadingFinanceList){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            CircularProgressIndicator()
                        }
                    } else if(loadingFinanceListError.isNotBlank()){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                            Text(loadingFinanceListError, fontSize = 15.sp, color = Color.Red)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    PullToRefreshBox(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            viewModel.getFinancesList(true)
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (grupos.isEmpty()) {
                            Text("No estás en ningún grupo aún.")
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(grupos) { grupo ->
                                    GrupoItem(grupo, navController, nombre, onClick = { id ->
                                        showLeaveFinanceDialog = true
                                        selectedFinance = id
                                    })
                                }
                            }
                        }
                    }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick = { showJoinDialog = true },
                    containerColor = Color(0xFF2E7D32)
                ) {
                    Icon(Icons.Default.Link, contentDescription = "Unirse a grupo")
                }
            }
        }
    }

    if (showJoinDialog) {
        JoinGroupDialog(
            onDismiss = { showJoinDialog = false },
            onJoin = { code ->
                viewModel.joinFinance(code)
            },
            finanzasViewModel = viewModel,
            errorMessage = joiningError
        )
    }


    if(showLeaveFinanceDialog){
        LeaveFinanceDialog(
            onDismiss = {
                showLeaveFinanceDialog = false
            },
            onLeave = {
                viewModel.leaveFinance(finanzaId = selectedFinance)
            },
            viewModel
        )
    }

    if (showCreateDialog) {
        CreateGroupDialog(
            onDismiss = {
                showCreateDialog = false
                selectedFinance = 0
                        },
            onCreate = { titulo, descripcion ->
                viewModel.createFinance(titulo, descripcion)
            },
            finanzasViewModel = viewModel,
            errorMessage = creatingError
        )
    }
}

@Composable
fun GrupoItem(
    grupo: FinancesListResponseDomain,
    navController: NavHostController,
    nombre: String,
    onClick: (Int) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                navController.navigate(FinanzaIndividualScreen(grupo.finanza_id, grupo.finanza_nombre))
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column {
                        Text(
                            text = grupo.finanza_nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = grupo.nombre_admin,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    if(nombre != grupo.nombre_admin) {
                        IconButton(
                            onClick = { onClick(grupo.finanza_id) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = Color(0xFF3D4B4E)
                            )
                        }
                    }
                }
            }
    }
}
