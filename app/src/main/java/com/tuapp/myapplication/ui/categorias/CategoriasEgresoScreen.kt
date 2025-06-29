package com.tuapp.myapplication.ui.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun CategoriasEgresoScreen(
    navController: NavController,
    finanzaId: Int?,
    categoryViewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)
    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    val categorias by categoryViewModel.categoriesList.collectAsStateWithLifecycle()
    val loadingCategories by categoryViewModel.loadingCategories.collectAsStateWithLifecycle()

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var nuevaCategoria by rememberSaveable { mutableStateOf("") }
    var selectedCategorieId by rememberSaveable { mutableIntStateOf(0) }
    var selectedCategorieName by rememberSaveable { mutableStateOf("") }

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by categoryViewModel.isRefreshing.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        categoryViewModel.getCategoriesList(finanzaId)
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.CATEGORIAS_EGRESO, navController, true)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    nuevaCategoria = ""
                    showDialog = true
                },
                containerColor = Color(0xFF2E7D32),
                modifier = Modifier.padding(end = 24.dp, bottom = 80.dp)
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
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
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
                    Spacer(modifier = Modifier.height(45.dp))

                    if(loadingCategories) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    PullToRefreshBox(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        onRefresh = { categoryViewModel.getCategoriesList(finanzaId, true) }
                    ) {
                        if (categorias.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay categorias ingresadas.", fontSize = 16.sp, color = Color.Gray)
                            }
                        } else {
                            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()) {
                                items(categorias) { categoria ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .padding(vertical = 6.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA))
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                categoria.categoria_nombre,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 18.sp
                                            )

                                            IconButton(
                                                onClick = {
                                                    showEditDialog = true
                                                    selectedCategorieId = categoria.categoria_id
                                                    selectedCategorieName = categoria.categoria_nombre
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
                    }
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                categoryViewModel.createCategory(nuevaCategoria, finanzaId)
                                showDialog = false
                            }) {
                                Text("Agregar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancelar")
                            }
                        },
                        title = { Text("Nueva Categoria") },
                        text = {
                            OutlinedTextField(
                                value = nuevaCategoria,
                                onValueChange = { nuevaCategoria = it },
                                label = { Text("Nombre de categoria") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                } else if(showEditDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showEditDialog = false
                            selectedCategorieId = 0
                                           },
                        confirmButton = {
                            TextButton(onClick = {
                                categoryViewModel.updateCategory(selectedCategorieName, selectedCategorieId)
                                showEditDialog = false
                                selectedCategorieId = 0
                            }) {
                                Text("Actualizar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showEditDialog = false
                                selectedCategorieId = 0
                            }) {
                                Text("Cancelar")
                            }
                        },
                        title = { Text("Editar Categoria") },
                        text = {
                            OutlinedTextField(
                                value = selectedCategorieName,
                                onValueChange = { selectedCategorieName = it },
                                label = { Text("Nombre de categoria") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
        }
    }
}



