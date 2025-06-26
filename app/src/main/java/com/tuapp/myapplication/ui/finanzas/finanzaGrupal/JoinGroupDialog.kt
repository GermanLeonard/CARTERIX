package com.tuapp.myapplication.ui.finanzas.finanzaGrupal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun JoinGroupDialog(
    onDismiss: () -> Unit,
    onJoin: (String) -> Unit
) {
    var codigo by rememberSaveable { mutableStateOf("") }

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
            Column {
                Text("Ingresa el código del grupo para unirte:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    placeholder = { Text("Código del grupo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
