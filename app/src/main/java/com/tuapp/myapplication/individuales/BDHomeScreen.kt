package com.tuapp.myapplication.individuales

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.components.BottomNavBar
import com.tuapp.myapplication.navigation.Routes

@Composable
fun BDHomeScreen(navController: NavController) {
    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)
    val verdePastel = Color(0xFFE6F4EA)
    val currentRoute = Routes.BD_HOME

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(verde),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Finanza principal",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 60.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                )
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(verdePastel, RoundedCornerShape(50))
                    .border(BorderStroke(2.dp, verde), shape = RoundedCornerShape(50))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Analisis", "Transacciones", "BD").forEach { tab ->
                    val isSelected = tab == "BD"
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .background(
                                if (isSelected) verdeClaro else Color.Transparent,
                                RoundedCornerShape(50)
                            )
                            .clickable {
                                when (tab) {
                                    "Analisis" -> navController.navigate(Routes.INDIVIDUAL)
                                    "Transacciones" -> {}
                                    "BD" -> {}
                                }
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BDCard("Categorias Egreso") { navController.navigate(Routes.CATEGORIAS_EGRESO) }
                    BDCard("SubCategorias") { navController.navigate(Routes.SUBCATEGORIAS) }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BDCard("Ingresos") { navController.navigate(Routes.INGRESOS) }
                    BDCard("Ahorro") { /* futura navegacion */ }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

@Composable
fun BDCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .size(width = 160.dp, height = 160.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(title, fontWeight = FontWeight.Medium)
        }
    }
}
