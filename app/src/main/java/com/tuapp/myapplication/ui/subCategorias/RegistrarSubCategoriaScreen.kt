package com.tuapp.myapplication.ui.subCategorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.categorias.CategoriesViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun RegistrarSubcategoriaScreen(
    navController: NavController,
    categoriasViewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
) {

    val categoriesOptions by categoriasViewModel.categoriesOptions.collectAsStateWithLifecycle()
    val tiposGasto = listOf("Gasto Fijo", "Gasto Variable")

    var categoriaPadre by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var tipoGasto by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf("") }

    var showCategoriaMenu by remember { mutableStateOf(false) }
    var showTipoMenu by remember { mutableStateOf(false) }

    val verde = Color(0xFF2E7D32)
    val currentRoute = Routes.BD_HOME

    LaunchedEffect(Unit) {
        categoriasViewModel.getCategoriesOptions()
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
                Text(
                    text = "Registrar\nSubCategoria",
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .padding(20.dp)
                    .weight(1f)
            ) {
                Text("Categoria Padre")
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6), shape = RoundedCornerShape(8.dp))
                    .clickable { showCategoriaMenu = !showCategoriaMenu }
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = if (categoriaPadre.isNotEmpty()) categoriaPadre else "Selecciona la categoria Padre")
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                if (showCategoriaMenu) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(4.dp)
                    ) {
                        TODO("USAR EL ID AQUI PARA REGISTRAR LA SUB CATEGORIA, NO EL NOMBRE")
                        categoriesOptions.forEach {
                            Text(
                                text = it.categoria_nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        categoriaPadre = it.categoria_nombre
                                        showCategoriaMenu = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Nombre de Subcategoria")
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    placeholder = { Text("Ej: Gasolina") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Tipo de Gasto")
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6), shape = RoundedCornerShape(8.dp))
                    .clickable { showTipoMenu = !showTipoMenu }
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = if (tipoGasto.isNotEmpty()) tipoGasto else "Selecciona el tipo de gasto")
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                if (showTipoMenu) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(4.dp)
                    ) {
                        tiposGasto.forEach {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        tipoGasto = it
                                        showTipoMenu = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Presupuesto Mensual")
                TextField(
                    value = presupuesto,
                    onValueChange = { presupuesto = it },
                    placeholder = { Text("Ej: 100.0") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            //subcategoriaViewModel.agregar(
                            //Subcategoria(
                            //       categoriaPadre = categoriaPadre,
                            //        nombre = nombre,
                            //        tipo = tipoGasto,
                            //        presupuesto = presupuesto.toDoubleOrNull() ?: 0.0
                            //    )
                            //)
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = verde)
                    ) {
                        Text("Registrar")
                    }

                    OutlinedButton(onClick = { navController.popBackStack() }) {
                        Text("Cancelar", color = verde)
                    }
                }
            }
        }


        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}








