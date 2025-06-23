package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tuapp.myapplication.data.models.financeModels.response.FinancesListResponseDomain
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun GrupalFinanceScreen(
    navController: NavHostController,
    viewModel: FinanzasViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.getFinancesList()
    }

    val grupos by viewModel.listaGrupos.collectAsState()
    var showJoinDialog by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Routes.GROUP)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Header verde
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2E7D32))
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Lista de Finanzas",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

                Column(modifier = Modifier.fillMaxSize()) {

                    if (grupos.isEmpty()) {
                        Text("No estás en ningún grupo aún.")
                    } else {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(grupos) { grupo ->
                                GrupoItem(grupo)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FloatingActionButton(
                            onClick = { showJoinDialog = true },
                            containerColor = Color(0xFF2E7D32)
                        ) {
                            Icon(Icons.Default.Link, contentDescription = "Unirse a grupo")
                        }
                        FloatingActionButton(
                            onClick = { showCreateDialog = true },
                            containerColor = Color(0xFF2E7D32)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Crear grupo")
                        }
                    }
                }
            }
        }
    }

    if (showJoinDialog) {
        JoinGroupDialog(
            onDismiss = { showJoinDialog = false },
            onJoin = { code ->
                viewModel.joinFinance(code)
                showJoinDialog = false
            }
        )
    }

    if (showCreateDialog) {
        CreateGroupDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { titulo ->
                viewModel.createFinance(titulo, "")
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun GrupoItem(grupo: FinancesListResponseDomain) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
    }
}
