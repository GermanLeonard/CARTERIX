package com.tuapp.myapplication.ui.savings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuapp.myapplication.data.models.savingsModels.request.CreateOrUpdateSavingDomain
import com.tuapp.myapplication.ui.savings.SavingsViewModel
import java.util.Calendar

@Composable
fun NuevaMetaDialog(
    onDismiss: () -> Unit,
    onCreate: (CreateOrUpdateSavingDomain) -> Unit,
    savingsViewModel: SavingsViewModel,
    errorMessage: String,
) {
    var monto by rememberSaveable { mutableStateOf("") }
    var mes by rememberSaveable { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val anio = calendar.get(Calendar.YEAR)

    val loadingCreating by savingsViewModel.loadingCreating.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Meta") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if(loadingCreating){
                    CircularProgressIndicator()
                }else {
                    Text("Si intentas crear una meta ya registrada se reemplazara")

                    OutlinedTextField(
                        value = monto,
                        onValueChange = { monto = it },
                        label = { Text("Monto") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = mes,
                        onValueChange = { mes = it },
                        label = { Text("Mes (1-12)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if(errorMessage.isNotBlank()){
                        Text(errorMessage, fontSize = 15.sp, color = Color.Red)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if(mes.isNotBlank() && monto.isNotBlank()){
                    val meta = CreateOrUpdateSavingDomain(
                        mes = mes.toInt(),
                        anio = anio,
                        monto = monto.toDouble()
                    )
                    onCreate(meta)
                }
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
