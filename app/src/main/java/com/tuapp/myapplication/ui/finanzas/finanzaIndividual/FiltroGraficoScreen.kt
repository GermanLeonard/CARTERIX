package com.tuapp.myapplication.ui.finanzas.finanzaIndividual

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tuapp.myapplication.data.models.categoryModels.response.CategoriesListDomain
import com.tuapp.myapplication.data.models.categoryModels.response.CategorieDataResponseDomain
import com.tuapp.myapplication.ui.categorias.CategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltroGraficoScreen(
    navController: NavController,
    finanzaId: Int,
    nombreFinanza: String
) {
    val gradientColors = listOf(
        Color(0xFF1B5E20),
        Color(0xFF2E7D32),
        Color(0xFF4CAF50)
    )

    val viewModel: CategoriesViewModel = viewModel(factory = CategoriesViewModel.Factory)
    val categories by viewModel.categoriesList.collectAsState()
    val categoryDetails by viewModel.categoryDetails.collectAsState()
    val loadingDetails by viewModel.loadingDetails.collectAsState()

    var selectedCategory by remember { mutableStateOf<CategoriesListDomain?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getCategoriesList(finanzaId)
    }

    LaunchedEffect(selectedCategory?.categoria_id) {
        selectedCategory?.categoria_id?.let { categoryId ->
            viewModel.getCategoryDetails(categoryId, finanzaId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ANÁLISIS POR CATEGORÍAS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(gradientColors)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Espacio vacío decorativo
                Spacer(modifier = Modifier.height(16.dp))

                // Contenido principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                        .background(
                            color = Color(0xFFF8F9FA),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(16.dp)
                ) {
                    // Lista de categorías
                    EnhancedCategoriesList(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelect = { selectedCategory = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Detalles de la categoría seleccionada
                    AnimatedVisibility(
                        visible = selectedCategory != null,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        when {
                            loadingDetails -> {
                                LoadingCard()
                            }
                            categoryDetails != null -> {
                                EnhancedCategorySummary(categoryDetails!!)
                            }
                            else -> {
                                EmptyStateCard()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedCategoriesList(
    categories: List<CategoriesListDomain>,
    selectedCategory: CategoriesListDomain?,
    onCategorySelect: (CategoriesListDomain) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Category,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Categorías de Gastos",
                    fontSize = 18.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    EnhancedCategoryItem(
                        category = category,
                        isSelected = selectedCategory?.categoria_id == category.categoria_id,
                        onSelect = { onCategorySelect(category) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedCategoryItem(
    category: CategoriesListDomain,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF4CAF50) else Color(0xFFF5F5F5),
        animationSpec = tween(300)
    )

    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) 4.dp else 0.dp,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(animatedElevation, RoundedCornerShape(12.dp))
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = animatedColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.3f) else Color(0xFFE0E0E0),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Category,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color(0xFF757575),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = category.categoria_nombre,
                fontSize = 16.sp,
                color = if (isSelected) Color.White else Color(0xFF212121),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Seleccionar",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedCategorySummary(category: CategorieDataResponseDomain) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header del resumen
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    Icons.Default.Analytics,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Resumen Financiero",
                    fontSize = 20.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }

            // Métricas principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricCard(
                    title = "Presupuesto",
                    value = "${"%.2f".format(category.presupuesto_total)}",
                    icon = Icons.Default.AccountBalance,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                MetricCard(
                    title = "Gastado",
                    value = "${"%.2f".format(category.gasto_total)}",
                    icon = Icons.Default.ShoppingCart,
                    color = Color(0xFFFF9800),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                MetricCard(
                    title = "Balance",
                    value = "${"%.2f".format(category.diferencia_total)}",
                    icon = if (category.diferencia_total >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                    color = when {
                        category.diferencia_total > 0 -> Color(0xFF4CAF50)
                        category.diferencia_total < 0 -> Color(0xFFF44336)
                        else -> Color(0xFF757575)
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Subcategorías
            if (category.sub_categorias.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        Icons.Default.AccountTree,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Desglose por Subcategorías",
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }

                category.sub_categorias.forEach { subcategory ->
                    EnhancedSubcategoryItem(
                        name = subcategory.nombre_sub_categoria,
                        presupuesto = subcategory.presupuesto_sub_categoria,
                        gasto = subcategory.gasto_sub_categoria,
                        diferencia = subcategory.diferencia_sub_categoria
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF616161),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontSize = 14.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EnhancedSubcategoryItem(
    name: String,
    presupuesto: Double,
    gasto: Double,
    diferencia: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.SubdirectoryArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = name,
                    fontSize = 15.sp,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${"%.2f".format(presupuesto)}",
                        fontSize = 14.sp,
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Presup.",
                        fontSize = 10.sp,
                        color = Color(0xFF757575)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${"%.2f".format(gasto)}",
                        fontSize = 14.sp,
                        color = Color(0xFFFF9800),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Gasto",
                        fontSize = 10.sp,
                        color = Color(0xFF757575)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${"%.2f".format(diferencia)}",
                        fontSize = 14.sp,
                        color = when {
                            diferencia > 0 -> Color(0xFF4CAF50)
                            diferencia < 0 -> Color(0xFFF44336)
                            else -> Color(0xFF757575)
                        },
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Balance",
                        fontSize = 10.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cargando detalles...",
                    fontSize = 14.sp,
                    color = Color(0xFF616161)
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Analytics,
                contentDescription = null,
                tint = Color(0xFFE0E0E0),
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selecciona una categoría",
                fontSize = 16.sp,
                color = Color(0xFF616161),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "para ver su análisis detallado",
                fontSize = 14.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}