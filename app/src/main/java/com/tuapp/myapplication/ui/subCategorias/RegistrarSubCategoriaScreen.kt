package com.tuapp.myapplication.ui.subCategorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.tuapp.myapplication.ui.categorias.CategoriesViewModel
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun RegistrarSubcategoriaScreen(
    navController: NavController,
    finanzaId: Int?,
    categoriasViewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory),
    subCategoriaViewModel: SubCategoriesViewModel = viewModel(factory = SubCategoriesViewModel.Factory),
) {

    val categoriesOptions by categoriasViewModel.categoriesOptions.collectAsStateWithLifecycle()
    val gastoOpciones by subCategoriaViewModel.categoriesExpenses.collectAsStateWithLifecycle()

    var categoriaPadre by rememberSaveable { mutableStateOf("") }
    //id de la categoria que se usara para la peticion
    var categoriaId by rememberSaveable { mutableIntStateOf(0) }

    var nombre by rememberSaveable { mutableStateOf("") }

    var tipoGasto by rememberSaveable { mutableStateOf("") }
    //id del tipo de gasto que se usara para la peticion
    var gastoId by rememberSaveable { mutableIntStateOf(0) }

    var presupuesto by rememberSaveable { mutableStateOf("") }

    var showCategoriaMenu by rememberSaveable { mutableStateOf(false) }
    var showTipoMenu by rememberSaveable { mutableStateOf(false) }

    val verde = Color(0xFF2E7D32)
    val currentRoute = Routes.BD_HOME

    LaunchedEffect(Unit) {
        categoriasViewModel.getCategoriesOptions(finanzaId)
        subCategoriaViewModel.getExpensesOptions()
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.REGISTRAR_SUBCATEGORIA, navController, true)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        contentColor = Color.Black,
        containerColor = verde
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

                Spacer(modifier = Modifier.height(25.dp))

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
                            Text(text = categoriaPadre.ifEmpty { "Selecciona la categoria Padre" })
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
                            categoriesOptions.forEach {
                                Text(
                                    text = it.categoria_nombre,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            categoriaPadre = it.categoria_nombre
                                            categoriaId = it.categoria_id
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
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
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
                            Text(text = tipoGasto.ifEmpty { "Selecciona el tipo de gasto" })
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
                            gastoOpciones.forEach {
                                Text(
                                    text = it.tipo_nombre,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            tipoGasto = it.tipo_nombre
                                            gastoId = it.tipo_id
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
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                subCategoriaViewModel.createSubCategory(
                                    idCategoria = categoriaId,
                                    nombreSubCategoria = nombre,
                                    presupuestoMensual = presupuesto.toDouble(),
                                    tipoGastoId = gastoId,
                                    finanzaId = finanzaId
                                )
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
    }
}








