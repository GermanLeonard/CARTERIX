package com.tuapp.myapplication.ui.subCategorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.tuapp.myapplication.ui.components.BottomNavBar
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.categorias.CategoriesViewModel
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.DetalleSubCategoriaRoute
import com.tuapp.myapplication.ui.navigation.RegistrarSubCategoriaScreen
import com.tuapp.myapplication.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcategoriasScreen(
    navController: NavController,
    finanzaId: Int?,
    categoriasViewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory),
    subCategoryViewModel: SubCategoriesViewModel = viewModel(factory = SubCategoriesViewModel.Factory)
) {
    val subCategoriasList by subCategoryViewModel.subCategoriesList.collectAsStateWithLifecycle()
    val categories by categoriasViewModel.categoriesOptions.collectAsStateWithLifecycle()

    val isLoading by subCategoryViewModel.isLoading.collectAsStateWithLifecycle()
    val mensajeError by subCategoryViewModel.mensajeError.collectAsStateWithLifecycle()

    var selectedCategoria by rememberSaveable { mutableStateOf("") }
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing by subCategoryViewModel.isRefreshing.collectAsStateWithLifecycle()

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL
    val verde = Color(0xFF2E7D32)

    LaunchedEffect(Unit) {
        categoriasViewModel.getCategoriesOptions(finanzaId)
        subCategoryViewModel.getSubCategoriesList(finanzaId)
    }

    Scaffold(
        topBar = {
            CustomTopBar(Routes.SUBCATEGORIAS, navController, true)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(RegistrarSubCategoriaScreen(finanzaId ?: 0)) },
                containerColor = verde,
                modifier = Modifier
                    .padding(end = 24.dp, bottom = 100.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
            }
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
                Spacer(modifier = Modifier.height(25.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE8EAF6), shape = RoundedCornerShape(8.dp))
                        .clickable { showMenu = !showMenu }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = selectedCategoria.ifEmpty { "Filtrar por Categoria" })
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }

                if (showMenu) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Seleccionar todos",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategoria = ""
                                    showMenu = false
                                }
                                .padding(12.dp)
                        )
                        categories.forEach {
                            Text(
                                text = it.categoria_nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedCategoria = it.categoria_nombre
                                        showMenu = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if(mensajeError.isNotBlank()){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(mensajeError, fontSize = 15.sp, color = Color.Red)
                    }
                }

                    // Mostrar lista o mensaje de carga
                    PullToRefreshBox(
                        state = pullToRefreshState,
                        isRefreshing = isRefreshing,
                        onRefresh = {
                            subCategoryViewModel.getSubCategoriesList(finanzaId, true)
                        }
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(subCategoriasList.filter {
                                selectedCategoria.isEmpty() || it.categoria_nombre == selectedCategoria
                            }) { sub ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable {
                                            navController.navigate(
                                                DetalleSubCategoriaRoute(
                                                    subcategoriaId = sub.sub_categoria_id,
                                                    finanzaId = finanzaId ?: 0
                                                )
                                            )
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                sub.categoria_nombre,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text("${sub.sub_categoria_nombre} - ${sub.tipo_gasto}")
                                            Text("Presupuesto: $${sub.presupuesto}", color = verde)
                                        }
                                        if (finanzaId != null) {
                                            Text(
                                                text = sub.nombre_usuario,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                fontSize = 12.sp,
                                                color = Color.Gray
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



