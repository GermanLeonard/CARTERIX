package com.tuapp.myapplication.ui.savings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain

@Composable
fun NuevaMetaDialog(
    onDismiss: () -> Unit,
    onCreate: (CreateOrUpdateSavingDomain) -> Unit
) {
    var monto by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Meta") },
        text = {
            Column {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = mes,
                    onValueChange = { mes = it },
                    label = { Text("Mes (1-12)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = anio,
                    onValueChange = { anio = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val meta = CreateOrUpdateSavingDomain(
                    mes = mes.toIntOrNull() ?: 1,
                    anio = anio.toIntOrNull() ?: 2025,
                    monto = monto.toDoubleOrNull() ?: 0.0
                )
                onCreate(meta)
                onDismiss()
            }) {
                Text("Registrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
