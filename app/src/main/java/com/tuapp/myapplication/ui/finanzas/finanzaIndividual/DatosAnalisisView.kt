package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tuapp.myapplication.data.models.financeModels.response.CategorieResponseDomain
import com.tuapp.myapplication.ui.navigation.FilterByCategoryScreen
import kotlin.math.max

@Composable
fun DatosAnalisisView(
    datosFinanza: List<CategorieResponseDomain>,
    navController: NavController,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFE9ECEF)
                    )
                )
            )
    ) {

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFF5F5F5),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Categoría",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF37474F),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Presupuesto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF37474F),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Gasto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF37474F),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Diferencia",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF37474F),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    datosFinanza.forEachIndexed { index, categoria ->
                        val colorDiferencia = when {
                            categoria.diferencia > 0 -> Color(0xFF4CAF50)
                            categoria.diferencia < 0 -> Color(0xFFF44336)
                            else -> Color(0xFF9E9E9E)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (index % 2 == 0) Color.Transparent else Color(0xFFFAFAFA),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                categoria.categoria_nombre,
                                fontSize = 13.sp,
                                color = Color(0xFF212121),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "$%.2f".format(categoria.total_presupuesto),
                                color = Color(0xFF7B1FA2),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "$%.2f".format(categoria.gasto),
                                color = Color(0xFF7B1FA2),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "$%.2f".format(categoria.diferencia),
                                color = colorDiferencia,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (index < datosFinanza.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE0E0E0)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                "Presupuesto vs Gasto Real",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF37474F)
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                Color(0xFF3F51B5),
                                RoundedCornerShape(3.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Presupuesto",
                        fontSize = 13.sp,
                        color = Color(0xFF37474F),
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                Color(0xFF4CAF50),
                                RoundedCornerShape(3.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Gasto Real",
                        fontSize = 13.sp,
                        color = Color(0xFF37474F),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Filtrar gráfico",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(20.dp)
                            .clickable {
                                navController.navigate(
                                    FilterByCategoryScreen(
                                        finanzaId = datosFinanza.firstOrNull()?.finanza_id ?: 0,
                                        nombreFinanza = "Filtrado"
                                    )
                                )
                            },
                        tint = Color(0xFFBDBDBD)
                    )

                    BarChart(
                        datos = datosFinanza,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(calculateChartHeight(datosFinanza.size))
                            .padding(top = 32.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun calculateChartHeight(itemCount: Int): androidx.compose.ui.unit.Dp {
    val topPadding = 50
    val bottomPadding = 30
    val barHeight = 18
    val spaceBetweenPairs = 40
    val spaceBetweenBars = 6

    val totalHeight = topPadding + (itemCount * (barHeight * 2 + spaceBetweenBars + spaceBetweenPairs)) + bottomPadding

    return totalHeight.dp
}

@Composable
fun BarChart(datos: List<CategorieResponseDomain>, modifier: Modifier = Modifier) {
    val maxVal = max(
        datos.maxOfOrNull { it.total_presupuesto } ?: 0.0,
        datos.maxOfOrNull { it.gasto } ?: 0.0
    )

    Canvas(modifier = modifier) {
        val barHeight = 18.dp.toPx()
        val spaceBetweenPairs = 40.dp.toPx()
        val spaceBetweenBars = 6.dp.toPx()
        val sidePadding = 20.dp.toPx()
        val labelPadding = 12.dp.toPx()
        val maxBarWidth = size.width - sidePadding * 2 - 120.dp.toPx()

        val startY = 30.dp.toPx()

        datos.forEachIndexed { index, item ->
            val y = startY + index * (barHeight * 2 + spaceBetweenBars + spaceBetweenPairs)

            val presupuestoWidth = if (maxVal > 0) {
                ((item.total_presupuesto / maxVal).toFloat()).coerceAtMost(1f) * maxBarWidth
            } else 0f

            val gastoWidth = if (maxVal > 0) {
                ((item.gasto / maxVal).toFloat()).coerceAtMost(1f) * maxBarWidth
            } else 0f

            val textPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#37474F")
                textSize = 26f
                isAntiAlias = true
            }

            val labelPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#616161")
                textSize = 24f
                isAntiAlias = true
            }

            drawContext.canvas.nativeCanvas.drawText(
                item.categoria_nombre,
                sidePadding,
                y - 10f,
                textPaint
            )

            if (presupuestoWidth > 0) {
                drawRoundRect(
                    color = Color(0xFF3F51B5),
                    topLeft = Offset(sidePadding, y),
                    size = Size(presupuestoWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
                drawContext.canvas.nativeCanvas.drawText(
                    "$${String.format("%.0f", item.total_presupuesto)}",
                    sidePadding + presupuestoWidth + labelPadding,
                    y + barHeight / 1.3f,
                    labelPaint
                )
            }

            if (gastoWidth > 0) {
                drawRoundRect(
                    color = Color(0xFF4CAF50),
                    topLeft = Offset(sidePadding, y + barHeight + spaceBetweenBars),
                    size = Size(gastoWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                )
                drawContext.canvas.nativeCanvas.drawText(
                    "$${String.format("%.0f", item.gasto)}",
                    sidePadding + gastoWidth + labelPadding,
                    y + barHeight + spaceBetweenBars + barHeight / 1.3f,
                    labelPaint
                )
            }
        }
    }
}