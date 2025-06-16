package com.tuapp.myapplication.ui.subCategorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.data.database.AppDatabase
import com.tuapp.myapplication.ui.viewmodel.CategoriaEgresoViewModel
import com.tuapp.myapplication.ui.viewmodel.CategoriaEgresoViewModelFactory
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.categorias.CategoriesViewModel
import com.tuapp.myapplication.ui.navigation.RegistrarSubCategoriaScreen
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun SubcategoriasScreen(
    navController: NavController,
    categoriasViewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory),
    subCategoryViewModel: SubCategoriesViewModel = viewModel(factory = SubCategoriesViewModel.Factory)
) {
    val subCategoriasList by subCategoryViewModel.subCategoriesList.collectAsStateWithLifecycle()
    val categories by categoriasViewModel.categoriesOptions.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        //ESTO ES DE PRUEBA
        categoriasViewModel.getCategoriesOptions()
        subCategoryViewModel.getSubCategoriesList()
    }

    var selectedCategoria by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    val currentRoute = Routes.SUBCATEGORIAS
    val verde = Color(0xFF2E7D32)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(verde),
                contentAlignment = Alignment.Center
            ) {
                Text("Subcategorias", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            // Filtro
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)) {

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
                        Text(text = if (selectedCategoria.isNotEmpty()) selectedCategoria else "Filtrar por Categoria")
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
                        categories.forEach {
                            //UNA VEZ ELEGIDO TIENE QUE SER FILTRADO EN LA LISTA QUE REGRESA EL VIEWMODEL
                            //POR MEDIO DEL NOMBRE CATEGORIA
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

                TODO("IMPLEMENTAR ESTO CON LA LISTA QUE TE DI ARRIBA Y EL ESATADO DEL FILTRO")
                //LazyColumn(modifier = Modifier.fillMaxSize()) {
                //    items(subcategorias.filter {
                //        selectedCategoria.isEmpty() || it.categoriaPadre == selectedCategoria
                //    }) { sub ->
                //        Card(
                //            modifier = Modifier
                //                .fillMaxWidth()
                //                .padding(vertical = 6.dp),
                //            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                //        ) {
                //            Column(modifier = Modifier.padding(12.dp)) {
                //                Text("${sub.categoriaPadre}", color = Color.Black, fontWeight = FontWeight.Bold)
                //                Text("${sub.nombre} - ${sub.tipo}")
                //               Text("Presupuesto: $${sub.presupuesto}", color = verde)
                //            }
                //        }
                //    }
                //}
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(RegistrarSubCategoriaScreen) },
            containerColor = verde,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.White)
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

