package com.tuapp.myapplication.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.R
import com.tuapp.myapplication.ui.navigation.FinanzaIndividualScreen
import com.tuapp.myapplication.ui.navigation.RegisterScreen

@Composable
fun LoginScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var correoError by rememberSaveable { mutableStateOf<String?>(null) }
    var contrasenaError by rememberSaveable { mutableStateOf<String?>(null) }

    val isLoggedIn by userViewModel.isLoggedIn.collectAsStateWithLifecycle()

    val loginLoading by userViewModel.loginLoading.collectAsStateWithLifecycle()

    val emailError by userViewModel.emailError.collectAsStateWithLifecycle()
    val passwordError by userViewModel.passwordError.collectAsStateWithLifecycle()
    val apiError by userViewModel.apiError.collectAsStateWithLifecycle()

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
                    painter = painterResource(id = R.drawable.carterix),
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
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if (emailError.isBlank() && apiError.isBlank()) {
                Text(correoError ?: "", color = Color.Red, fontSize = 12.sp)
            } else if(apiError.isNotBlank()) {
                Text(apiError, color = Color.Red, fontSize = 12.sp)
            }else {
                Text(emailError, color = Color.Red, fontSize = 12.sp)
            }

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
            if(passwordError.isBlank() && apiError.isBlank()){
                Text(contrasenaError ?: "", color = Color.Red, fontSize = 12.sp)
            } else if(apiError.isNotBlank()) {
                Text(apiError, color = Color.Red, fontSize = 12.sp)
            } else {
                Text(passwordError, color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón
            Button(
                onClick = {

                    val correoRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

                    var valido = true

                    if (!email.matches(correoRegex)) {
                        correoError = "Correo invalido"
                        valido = false
                    }
                    if (password.isBlank()) {
                        contrasenaError = "Escribe tu contraseña"
                        valido = false
                    }

                    if (valido) {
                        userViewModel.loginUser(email, password)
                    }
                    if(isLoggedIn){
                        navController.navigate(FinanzaIndividualScreen(0, ""))
                    }
                          },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = verde)
            ) {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
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

        if (loginLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.White),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

