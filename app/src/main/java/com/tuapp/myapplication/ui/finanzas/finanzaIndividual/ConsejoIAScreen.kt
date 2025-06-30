package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.ui.components.BottomNavBar
import com.tuapp.myapplication.ui.components.CustomTopBar
import com.tuapp.myapplication.ui.navigation.Routes

@Composable
fun ConsejoIAScreen(
    navController: NavController,
    finanzaId: Int?
) {

    val verde = Color(0xFF2E7D32)

    val currentRoute = if(finanzaId != null) Routes.GROUP else Routes.INDIVIDUAL

    Scaffold(
        topBar = {
            CustomTopBar(Routes.CONSEJO, navController = navController, true )
        },
        bottomBar = {
            BottomNavBar(navController, currentRoute)
        },
        contentColor = Color.Black,
        containerColor = verde
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
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
                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        // Acción al pedir el consejo
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = verde),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "Pedir consejo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Pedir consejo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}