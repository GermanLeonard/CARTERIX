package com.tuapp.myapplication.ui.subCategorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.tuapp.myapplication.ui.subCategorias.SubCategoriesViewModel

@Composable
fun DetalleSubCategoriaScreen(
    navController: NavController,
    subcategoriaId: Int,
    finanzaId: Int?,
    subCategoriesViewModel: SubCategoriesViewModel = viewModel(factory = SubCategoriesViewModel.Factory)
) {
    val verde = Color(0xFF2E7D32)

    val loadingDetails by subCategoriesViewModel.isLoading.collectAsStateWithLifecycle()
    val subcategoria by subCategoriesViewModel.subcategoriaDetalle.collectAsStateWithLifecycle()

    var categoriaNombre by rememberSaveable { mutableStateOf("") }
    var subCategoriaNombre by rememberSaveable { mutableStateOf("") }
    var tipoGasto by rememberSaveable { mutableStateOf("") }
    var presupuesto by rememberSaveable { mutableDoubleStateOf(0.0) }
    var nombreUsuario by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        subCategoriesViewModel.getSubCategoryDetails(subcategoriaId)
    }

    LaunchedEffect(subcategoria) {
        subcategoria?.let { detalle ->
            subCategoriaNombre = detalle.nombre_sub_categoria
            tipoGasto = when (detalle.tipo_gasto_id) {
                1 -> "Gasto Fijo"
                2 -> "Gasto Variable"
                else -> "Desconocido"
            }
            presupuesto = detalle.presupuesto
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar("Detalle Subcategoría", navController, true)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Routes.SUBCATEGORIAS)
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
                    .padding(horizontal = 16.dp)
            ) {
                if (loadingDetails) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Text("Categoría", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = categoriaNombre,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Subcategoría", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = subCategoriaNombre,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tipo de Gasto", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = tipoGasto,
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Presupuesto", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = "$$presupuesto",
                        onValueChange = {},
                        singleLine = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }
        }
    }
}