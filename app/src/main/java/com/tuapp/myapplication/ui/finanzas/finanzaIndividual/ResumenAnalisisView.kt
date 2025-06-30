package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuapp.myapplication.data.models.financeModels.response.ResumenAhorrosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenEgresosResponseDomain
import com.tuapp.myapplication.data.models.financeModels.response.ResumenFinancieroResponseDomain

@Composable
fun ResumenAnalisisView(
    resumenFinanciero: ResumenFinancieroResponseDomain,
    resumenEgresos: ResumenEgresosResponseDomain,
    resumenAhorro: ResumenAhorrosResponseDomain
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FA),
                        Color(0xFFE9ECEF)
                    )
                )
            )
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        // Balance General Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    "Balance General",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Ingresos
                    Box(modifier = Modifier.weight(1f)) {
                        FinancialMetricCard(
                            title = "Ingresos Totales",
                            amount = resumenFinanciero.ingresos_totales,
                            icon = Icons.Default.TrendingUp,
                            color = Color(0xFF4CAF50),
                            backgroundColor = Color(0xFFE8F5E8)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Egresos
                    Box(modifier = Modifier.weight(1f)) {
                        FinancialMetricCard(
                            title = "Egresos Totales",
                            amount = resumenFinanciero.egresos_totales,
                            icon = Icons.Default.TrendingDown,
                            color = Color(0xFFF44336),
                            backgroundColor = Color(0xFFFFEBEE)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Diferencia destacada
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (resumenFinanciero.diferencia >= 0)
                            Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (resumenFinanciero.diferencia >= 0)
                                Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (resumenFinanciero.diferencia >= 0)
                                Color(0xFF4CAF50) else Color(0xFFF44336),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Diferencia",
                                fontSize = 14.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "$${String.format("%.2f", resumenFinanciero.diferencia)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (resumenFinanciero.diferencia >= 0)
                                    Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Resumen Egresos Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF7B1FA2),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Resumen de Egresos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF3E5F5)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        EgresoMetricItem(
                            title = "Presupuesto",
                            amount = resumenEgresos.presupuesto_mensual,
                            color = Color(0xFF7B1FA2)
                        )

                        VerticalDivider(
                            modifier = Modifier.height(50.dp),
                            thickness = 1.dp,
                            color = Color(0xFFE1BEE7)
                        )

                        EgresoMetricItem(
                            title = "Consumo",
                            amount = resumenEgresos.consumo_mensual,
                            color = Color(0xFF7B1FA2)
                        )

                        VerticalDivider(
                            modifier = Modifier.height(50.dp),
                            thickness = 1.dp,
                            color = Color(0xFFE1BEE7)
                        )

                        EgresoMetricItem(
                            title = "Variación",
                            amount = resumenEgresos.variacion_mensual,
                            color = if (resumenEgresos.variacion_mensual >= 0)
                                Color(0xFF4CAF50) else Color(0xFFF44336),
                            showSign = true
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Meta de Ahorro Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Meta de Ahorro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        AhorroMetricCard(
                            title = "Meta Mensual",
                            amount = resumenAhorro.meta,
                            icon = Icons.Default.Flag,
                            color = Color(0xFF2196F3)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AhorroMetricCard(
                            title = "Ahorro Acumulado",
                            amount = resumenAhorro.acumulado,
                            icon = Icons.Default.AccountBalance,
                            color = Color(0xFF4CAF50)
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Gráfico circular mejorado
                    Box(
                        modifier = Modifier.size(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            val radius = size.minDimension / 2 - strokeWidth / 2

                            // Círculo de fondo
                            drawArc(
                                color = Color(0xFFE8F5E8),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Arco de progreso con gradiente
                            val sweepAngle = (resumenAhorro.progreso_porcentaje * 360f / 100).toFloat()
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        Color(0xFF4CAF50),
                                        Color(0xFF81C784)
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "${resumenAhorro.progreso_porcentaje.toInt()}%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                            Text(
                                "Progreso",
                                fontSize = 12.sp,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FinancialMetricCard(
    title: String,
    amount: Double,
    icon: ImageVector,
    color: Color,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                fontSize = 12.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )
            Text(
                "$${String.format("%.2f", amount)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun EgresoMetricItem(
    title: String,
    amount: Double,
    color: Color,
    showSign: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title,
            fontSize = 12.sp,
            color = Color(0xFF666666),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "${if (showSign && amount > 0) "+" else ""}$${String.format("%.2f", amount)}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun AhorroMetricCard(
    title: String,
    amount: Double,
    icon: ImageVector,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    title,
                    fontSize = 11.sp,
                    color = Color(0xFF666666),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "$${String.format("%.2f", amount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}