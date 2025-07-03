package com.tuapp.myapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.navigation.BDHomeScreen
import com.tuapp.myapplication.ui.navigation.FinanzaIndividualScreen
import com.tuapp.myapplication.ui.navigation.TransaccionesScreen

@Composable
fun TabSelector(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    navController: NavController,
    finanzaId: Int,
    nombreFinanza: String,
    tabs: List<String> = listOf("Analisis", "Transacciones", "BD"),
    backgroundColor: Color = Color(0xFF66BB6A),
    selectedColor: Color = Color(0xFF4CAF50)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(50))
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEach { label ->
            val isSelected = label == selectedTab

            val destination: Any? = when (label) {
                "Analisis" -> FinanzaIndividualScreen(finanzaId, nombreFinanza)
                "Transacciones" -> TransaccionesScreen(finanzaId, nombreFinanza)
                "BD" -> BDHomeScreen(finanzaId, nombreFinanza)
                else -> null
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(40))
                    .clickable {
                        if (!isSelected && destination != null) {
                            onTabSelected(label)
                            navController.navigate(destination)
                        } else {
                            onTabSelected(label)
                        }
                    }
                    .background(
                        if (isSelected) selectedColor else Color.Transparent
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
        }
    }
}