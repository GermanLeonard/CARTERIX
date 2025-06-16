package com.tuapp.myapplication.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.data.database.AppDatabase
import com.tuapp.myapplication.data.repository.CategoriaEgresoRepository
import com.tuapp.myapplication.ui.viewmodel.CategoriaEgresoViewModel
import com.tuapp.myapplication.ui.viewmodel.CategoriaEgresoViewModelFactory
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun CategoriasEgresoScreen(
    navController: NavController,
    categoryViewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
) {
    //QUITEN TODO ESTO, NO SE VA A USAR, USEN EL CATEGORIESVIEWMODEL DE AHI SAQUEN LA LISTA DE CATEGORIAS
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).categoriaEgresoDao()
    val repository = CategoriaEgresoRepository(dao)
    val viewModel: CategoriaEgresoViewModel = viewModel(factory = CategoriaEgresoViewModelFactory(repository))
    //ESTO YA NO

    val currentRoute = Routes.INDIVIDUAL
    //CAMBIEN ESTE VIEWMODEL POR EL OTRO, NO LO QUITO PORQUE SE ROMPE TODO
    val categorias by viewModel.categorias.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var nuevaCategoria by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        //AQUI TIENEN QUE HACER LA LLAMADA DEL VIEWMODEL PARA TENER LAS CATEGORIAS
        //ESTO ES PARA PROBAR
        categoryViewModel.getCategoriesList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Categorias de Egreso",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (categorias.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay categorias ingresadas.", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
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
                                    categoria.nombreCategoria,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                nuevaCategoria = ""
                showDialog = true
            },
            containerColor = Color(0xFF2E7D32),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 80.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
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
                        label = { Text("Nombre de categoria") }
                    )
                }
            )
        }
    }
}



