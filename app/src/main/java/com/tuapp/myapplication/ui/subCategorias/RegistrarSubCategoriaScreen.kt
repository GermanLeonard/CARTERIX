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

    var expandedTipoGasto by rememberSaveable { mutableStateOf(false) }
    var expandedCategoria by rememberSaveable { mutableStateOf(false) }

    var mensajeRegistro by rememberSaveable { mutableStateOf("") }

    val loadingCreating by subCategoriaViewModel.loadingCreating.collectAsStateWithLifecycle()
    val createdCategory by subCategoriaViewModel.createdCategory.collectAsStateWithLifecycle()
    val creatingError by subCategoriaViewModel.creatingError.collectAsStateWithLifecycle()

    val verde = Color(0xFF2E7D32)
    val currentRoute = Routes.BD_HOME

    LaunchedEffect(Unit) {
        categoriasViewModel.getCategoriesOptions(finanzaId)
        subCategoriaViewModel.getExpensesOptions()
    }

    LaunchedEffect(createdCategory) {
        if(createdCategory){
            navController.popBackStack()
        }
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

                if(loadingCreating){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center ){
                        CircularProgressIndicator()
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text("Categoria Padre")
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EAF6), shape = RoundedCornerShape(8.dp))
                    .clickable { expandedCategoria = true }
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = categoriaPadre.ifEmpty { "Selecciona la categoria Padre" })
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false}
                ) {
                    categoriesOptions.forEach{
                        DropdownMenuItem(
                            text = { Text(it.categoria_nombre)},
                            onClick = {
                                categoriaPadre = it.categoria_nombre
                                categoriaId = it.categoria_id
                                expandedCategoria = false
                            }
                        )
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
                    .clickable {expandedTipoGasto = true}
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = tipoGasto.ifEmpty { "Selecciona el tipo de gasto" })
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                DropdownMenu(
                    expanded = expandedTipoGasto,
                    onDismissRequest = {
                        expandedTipoGasto = false
                    }
                ) {
                    gastoOpciones.forEach {
                        DropdownMenuItem(
                            text = {Text(it.tipo_nombre)},
                            onClick = {
                                tipoGasto = it.tipo_nombre
                                gastoId = it.tipo_id
                                expandedTipoGasto = false
                            }
                        )
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

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if(creatingError.isBlank()){
                        Text(mensajeRegistro.ifBlank { "" }, fontSize = 12.sp, color = Color.Red)
                    } else {
                        Text(creatingError, fontSize = 12.sp, color = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if(categoriaId != 0 && nombre.isNotBlank() && presupuesto.isNotBlank() && gastoId != 0){
                                subCategoriaViewModel.createSubCategory(
                                    idCategoria = categoriaId,
                                    nombreSubCategoria = nombre,
                                    presupuestoMensual = presupuesto.toDouble(),
                                    tipoGastoId = gastoId,
                                    finanzaId = finanzaId
                                )
                            } else {
                                mensajeRegistro = "Completa todos los campos para registrar"
                            }
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








