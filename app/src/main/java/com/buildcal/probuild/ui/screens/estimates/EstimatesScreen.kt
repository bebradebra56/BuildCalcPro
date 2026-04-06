package com.buildcal.probuild.ui.screens.estimates

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun EstimatesScreen(navController: NavController) {
    val vm: EstimatesViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Filled.AttachMoney, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                        Text("Total Estimate", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Text("${state.currencySymbol}${"%.2f".format(state.totalCost)}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${state.materials.size} materials with pricing", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                }
            }
        }

        val pricedMaterials = state.materials.filter { it.pricePerUnit > 0 }
        if (pricedMaterials.isEmpty()) {
            item {
                EmptyStateMessage(
                    icon = Icons.Filled.AttachMoney,
                    title = "No priced materials",
                    subtitle = "Add prices to your materials to see cost estimates"
                )
            }
        } else {
            item {
                Text("Cost Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            items(pricedMaterials) { material ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(material.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text("${material.type} • ${"%.1f".format(material.quantity)} ${material.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${state.currencySymbol}${"%.2f".format(material.pricePerUnit)}/${material.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${state.currencySymbol}${"%.2f".format(material.pricePerUnit * material.quantity)}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            item {
                val percentage = if (state.totalCost > 0) pricedMaterials.map { it.pricePerUnit * it.quantity / state.totalCost } else emptyList()
                Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Cost Distribution", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        pricedMaterials.forEachIndexed { index, material ->
                            val pct = if (index < percentage.size) percentage[index] else 0.0
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(material.name, style = MaterialTheme.typography.bodySmall)
                                    Text("${"%.1f".format(pct * 100)}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                                }
                                LinearProgressIndicator(
                                    progress = { pct.toFloat() },
                                    modifier = Modifier.fillMaxWidth().height(6.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = { navController.navigate(Screen.Materials.route) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Edit, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Manage Material Prices")
            }
        }
    }
}
