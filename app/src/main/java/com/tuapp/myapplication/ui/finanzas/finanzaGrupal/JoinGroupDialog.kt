package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel

@Composable
fun JoinGroupDialog(
    onDismiss: () -> Unit,
    onJoin: (String) -> Unit,
    finanzasViewModel: FinanzasViewModel,
    errorMessage: String
) {
    var codigo by rememberSaveable { mutableStateOf("") }

    val loadingJoining by finanzasViewModel.loadingJoin.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (codigo.isNotBlank()) {
                        onJoin(codigo)
                    }
                }
            ) {
                Text("Unirse")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text("Unirse a un Grupo", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(loadingJoining){
                    CircularProgressIndicator()
                } else {
                    Text("Ingresa el código del grupo para unirte:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = codigo,
                        onValueChange = { codigo = it },
                        placeholder = { Text("Código del grupo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if(errorMessage.isNotBlank()){
                        Text(errorMessage, fontSize = 15.sp, color = Color.Red)
                    }
                }
            }
        }
    )
}
