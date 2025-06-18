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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun TabSelector(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    navController: NavController,
    tabs: List<String> = listOf("Analisis", "Transacciones", "BD"),
    backgroundColor: Color = Color(0xFFE6F4EA),
    borderColor: Color = Color(0xFF2E7D32),
    selectedColor: Color = Color(0xFF66BB6A)
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(50))
            .border(BorderStroke(2.dp, borderColor), shape = RoundedCornerShape(50))
            .padding(top = 6.dp, start = 3.dp, end = 3.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        tabs.forEach { label ->
            val isSelected = label == selectedTab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .background(
                        if (isSelected) selectedColor else Color.Transparent,
                        RoundedCornerShape(50)
                    )
                    .clickable {
                        onTabSelected(label)
                        when (label) {
                            "BD" -> navController.navigate(Routes.BD_HOME)
                            "Transacciones" -> navController.navigate(Routes.TRANSACCIONES)
                        }
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(label, color = Color.Black, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

