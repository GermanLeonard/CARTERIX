package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuapp.myapplication.data.models.financeModels.response.DatoAnalisisDomain
import com.tuapp.myapplication.ui.finanzas.FinanzasViewModel
import kotlin.math.max

@Composable
fun DatosAnalisisView(finanzasViewModel: FinanzasViewModel) {
    // Llamada para cargar los datos
    LaunchedEffect(Unit) {
        // Ajusta mes, año y finanza_id según tu lógica
        finanzasViewModel.financeData(mes = 6, anio = 2025, finanza_id = null)
    }

    val datos by finanzasViewModel.listaDatosAnalisis.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(16.dp))

        // Encabezado de tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Categoría", fontWeight = FontWeight.Bold)
            Text("Presupuesto", fontWeight = FontWeight.Bold)
            Text("Gasto", fontWeight = FontWeight.Bold)
            Text("Diferencia", fontWeight = FontWeight.Bold)
        }

        Divider(color = Color.LightGray, thickness = 1.dp)

        LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
            items(datos) { item: DatoAnalisisDomain ->
                val diferencia = item.presupuesto - item.gasto
                val colorDiferencia = when {
                    diferencia > 0 -> Color(0xFF2E7D32) // verde
                    diferencia < 0 -> Color(0xFFD32F2F) // rojo
                    else -> Color.Gray
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.categoria, modifier = Modifier.weight(1f))
                    Text("L. %.2f".format(item.presupuesto), color = Color(0xFF7B1FA2), modifier = Modifier.weight(1f))
                    Text("L. %.2f".format(item.gasto), color = Color(0xFF7B1FA2), modifier = Modifier.weight(1f))
                    Text(
                        "L. %.2f".format(diferencia),
                        color = colorDiferencia,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Presupuesto Vs Real",
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((datos.size * 50).dp + 60.dp)
                .padding(16.dp)
                .background(Color(0xFFEDE7F6), RoundedCornerShape(24.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "icon",
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                tint = Color.Gray
            )

            BarChart(datos, Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun BarChart(datos: List<DatoAnalisisDomain>, modifier: Modifier = Modifier) {
    val maxVal = max(
        datos.maxOfOrNull { it.presupuesto } ?: 0.0,
        datos.maxOfOrNull { it.gasto } ?: 0.0
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val barHeight = 20.dp.toPx()
        val space = 30.dp.toPx()
        val startY = 50f

        datos.forEachIndexed { index, item ->
            val y = startY + index * (barHeight + space)

            val presupuestoWidth = (item.presupuesto / maxVal).toFloat() * size.width
            val gastoWidth = (item.gasto / maxVal).toFloat() * size.width

            // Presupuesto: azul
            drawRoundRect(
                color = Color(0xFF3F51B5),
                topLeft = Offset(0f, y),
                size = Size(presupuestoWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
            )

            // Gasto: verde
            drawRoundRect(
                color = Color(0xFF4CAF50),
                topLeft = Offset(0f, y + barHeight + 4f),
                size = Size(gastoWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
            )
        }
    }
}
