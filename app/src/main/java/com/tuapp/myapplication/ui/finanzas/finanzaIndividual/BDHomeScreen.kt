package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.R
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.components.TabSelector
import com.tuapp.myapplication.ui.navigation.CategoriaEgresoScreen
import com.tuapp.myapplication.ui.navigation.IngresosScreen
import com.tuapp.myapplication.ui.navigation.Routes
import com.tuapp.myapplication.ui.navigation.SubCategoriaScreen
import com.tuapp.myapplication.ui.navigation.AhorroScreen


@Composable
fun BDHomeScreen(
    navController: NavController,
    finanzaId: Int,
    nombreFinanza: String
) {
    val verde = Color(0xFF2E7D32)
    val currentRoute = if(finanzaId != 0) Routes.GROUP else Routes.INDIVIDUAL
    var selectedTab by rememberSaveable { mutableStateOf("BD") }

    Scaffold(
        topBar = {
            CustomTopBar(nombreFinanza, navController)
        },
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        },
        containerColor = verde,
        contentColor = Color.Black
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                TabSelector(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    navController = navController,
                    finanzaId = finanzaId,
                    nombreFinanza = nombreFinanza
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BDCard("Categorias Egreso", R.drawable.categorias_logo, "categorias") { navController.navigate(CategoriaEgresoScreen(finanzaId)) }
                        BDCard("SubCategorias", R.drawable.sub_categorias, "sub categorias") { navController.navigate(SubCategoriaScreen(finanzaId)) }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BDCard("Ingresos", R.drawable.ingresos, "ingresos") { navController.navigate(IngresosScreen(finanzaId)) }
                        BDCard("Ahorro", R.drawable.ahorro, "ahorros") { navController.navigate(AhorroScreen(finanzaId)) }
                    }
            }
        }
    }
}

@Composable
fun BDCard(title: String, image: Int, description: String,  onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(12.dp)
                .size(width = 160.dp, height = 160.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(image),
                    contentDescription = description,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold  )
    }
}
