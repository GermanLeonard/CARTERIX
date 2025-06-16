package com.tuapp.myapplication.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.components.BottomNavBar

@Composable
fun EditProfileScreen(navController: NavController) {
    val verde = Color(0xFF2E7D32)

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasenaActual by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(verde),
                contentAlignment = Alignment.TopCenter
            ) {
                Text("Editar perfil", color = Color.White, fontSize = 22.sp, modifier = Modifier.padding(top = 32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Información de perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                CustomField("Nombre completo", nombre) { nombre = it }
                CustomField("Email", email) { email = it }

                Button(
                    onClick = { /* TODO: guardar nombre/email */ },
                    colors = ButtonDefaults.buttonColors(containerColor = verde),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Actualizar contraseña", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                CustomField("Contraseña actual", contrasenaActual) { contrasenaActual = it }
                CustomField("Nueva contraseña", nuevaContrasena) { nuevaContrasena = it }
                CustomField("Confirmar contraseña", confirmarContrasena) { confirmarContrasena = it }

                Button(
                    onClick = { /* TODO: cambiar contraseña */ },
                    colors = ButtonDefaults.buttonColors(containerColor = verde),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text("Guardar")
                }
            }
        }

        BottomNavBar(navController = navController, currentRoute = Routes.PROFILE)
    }
}

@Composable
fun CustomField(placeholder: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE6F4EA),
            unfocusedContainerColor = Color(0xFFE6F4EA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
