package com.buildcal.probuild.ui.screens.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.navigation.Screen
import com.buildcal.probuild.ui.theme.*

private data class CalculatorItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

private val calculators = listOf(
    CalculatorItem("Brick", "Wall area & brick count", Icons.Filled.ViewModule, BrickColor, Screen.BrickCalculator.route),
    CalculatorItem("Concrete", "Volume & mix ratios", Icons.Filled.Straighten, ConcreteColor, Screen.ConcreteCalculator.route),
    CalculatorItem("Tile", "Floor/wall tiling", Icons.Filled.GridOn, TileColor, Screen.TileCalculator.route),
    CalculatorItem("Paint", "Coverage & cans needed", Icons.Filled.FormatPaint, PaintColor, Screen.PaintCalculator.route),
    CalculatorItem("Drywall", "Panels & screws", Icons.Filled.TableChart, DrywallColor, Screen.DrywallCalculator.route),
    CalculatorItem("Insulation", "Rolls & coverage", Icons.Filled.Layers, InsulationColor, Screen.InsulationCalculator.route)
)

@Composable
fun CalculatorsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Calculators", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Choose a material to calculate", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(calculators) { calc ->
                CalculatorCard(calc = calc, onClick = { navController.navigate(calc.route) })
            }
        }
    }
}

@Composable
private fun CalculatorCard(calc: CalculatorItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(calc.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(calc.icon, contentDescription = null, tint = calc.color, modifier = Modifier.size(34.dp))
            }
            Spacer(Modifier.height(14.dp))
            Text(calc.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            Text(calc.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}
