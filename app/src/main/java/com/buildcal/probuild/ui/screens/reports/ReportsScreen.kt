package com.buildcal.probuild.ui.screens.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.datastore.SettingsDataStore
import com.buildcal.probuild.ui.screens.materials.materialTypeColor
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportsScreen(navController: NavController) {
    val vm: ReportsViewModel = koinViewModel()
    val state by vm.uiState.collectAsState(initial = ReportsUiState())
    val settingsDataStore = koinInject<SettingsDataStore>()
    val currencySymbol by settingsDataStore.currencySymbol.collectAsState(initial = "$")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Analytics Overview", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(Modifier.weight(1f), Icons.Filled.Folder, "Projects", state.projectCount.toString(), MaterialTheme.colorScheme.primary)
                SummaryCard(Modifier.weight(1f), Icons.Filled.Inventory, "Materials", state.materials.size.toString(), MaterialTheme.colorScheme.secondary)
                SummaryCard(Modifier.weight(1f), Icons.Filled.Calculate, "Calcs", state.calculations.size.toString(), MaterialTheme.colorScheme.tertiary)
            }
        }

        item {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Budget Estimate", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(Modifier.height(4.dp))
                    Text("$currencySymbol${"%.2f".format(state.totalCost)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        if (state.materialCountByType.isNotEmpty()) {
            item {
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Material Usage", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        val total = state.materialCountByType.values.sum().toFloat()
                        state.materialCountByType.entries.sortedByDescending { it.value }.forEach { (type, count) ->
                            val pct = if (total > 0) count / total else 0f
                            val typeColor = materialTypeColor(type)
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(type.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyMedium)
                                    Text("$count items (${"%.0f".format(pct * 100)}%)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                LinearProgressIndicator(progress = { pct }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = typeColor, trackColor = typeColor.copy(alpha = 0.2f))
                            }
                        }
                    }
                }
            }
        }

        if (state.calculations.isNotEmpty()) {
            item { Text("Recent Calculations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
            items(state.calculations.take(10)) { calc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(materialTypeColor(calc.type).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Calculate, null, tint = materialTypeColor(calc.type), modifier = Modifier.size(20.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(calc.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text(calc.type.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(SimpleDateFormat("MMM d", Locale.ENGLISH).format(Date(calc.createdAt)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, color: Color) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
