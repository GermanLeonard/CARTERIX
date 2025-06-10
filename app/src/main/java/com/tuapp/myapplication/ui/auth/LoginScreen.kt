package com.tuapp.myapplication.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.R
import com.tuapp.myapplication.ui.navigation.FinanzaIndividualScreen
import com.tuapp.myapplication.ui.navigation.RegisterScreen

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val verde = Color(0xFF2E7D32)
    val verdeClaro = Color(0xFF66BB6A)

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo verde superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(verde)
        )

        // Contenedor blanco con borde redondeado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)
                )
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    tint = verde,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Carterix",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = verde
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de bienvenida
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Bienvenido a Carterix",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tu espacio financiero e inteligente",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = verde,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = verde
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = verde,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = verde
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = verdeClaro,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón
            Button(
                onClick = { navController.navigate(FinanzaIndividualScreen) },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = verde)
            ) {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text("¿No tienes cuenta?", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = {
                    navController.navigate(RegisterScreen)
                }) {
                    Text("Crear cuenta", color = verdeClaro, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

