package com.buildcal.probuild.ui.screens.materials

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.buildcal.probuild.data.db.entity.MaterialEntity
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.SwipeToDeleteBox
import com.buildcal.probuild.ui.navigation.Screen
import com.buildcal.probuild.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

fun materialTypeColor(type: String): androidx.compose.ui.graphics.Color = when (type.lowercase()) {
    "brick" -> BrickColor
    "concrete" -> ConcreteColor
    "tile" -> TileColor
    "paint" -> PaintColor
    "drywall" -> DrywallColor
    "insulation" -> InsulationColor
    else -> androidx.compose.ui.graphics.Color(0xFF607D8B)
}

@Composable
fun MaterialsScreen(navController: NavController) {
    val vm: MaterialsViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    val settingsDataStore = koinInject<SettingsDataStore>()
    val currencySymbol by settingsDataStore.currencySymbol.collectAsState(initial = "$")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (state.materials.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Cost Estimate", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("$currencySymbol${"%.2f".format(state.totalCost)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Text("${state.materials.size} items", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }

            if (state.materials.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyStateMessage(icon = Icons.Filled.Inventory, title = "No materials yet", subtitle = "Tap + to add materials")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.materials, key = { it.id }) { material ->
                        SwipeToDeleteBox(onDelete = { vm.deleteMaterial(material) }) {
                            MaterialCard(
                                material = material,
                                currencySymbol = currencySymbol,
                                onClick = { navController.navigate(Screen.MaterialDetail.createRoute(material.id)) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddMaterial.route) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Material", tint = Color.White)
        }
    }
}

@Composable
fun MaterialCard(material: MaterialEntity, currencySymbol: String, onClick: () -> Unit) {
    val typeColor = materialTypeColor(material.type)
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)).background(typeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Inventory, null, tint = typeColor, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(material.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${material.type.uppercase()} • ${material.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (material.quantity > 0) {
                    Text("Qty: ${"%.1f".format(material.quantity)} ${material.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (material.pricePerUnit > 0) {
                    Text("$currencySymbol${"%.2f".format(material.pricePerUnit)}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text("per ${material.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (material.quantity > 0 && material.pricePerUnit > 0) {
                    Text("= $currencySymbol${"%.2f".format(material.quantity * material.pricePerUnit)}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
