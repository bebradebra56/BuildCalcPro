package com.buildcal.probuild.ui.screens.materials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.datastore.SettingsDataStore
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun MaterialDetailScreen(navController: NavController, materialId: Long) {
    val vm: MaterialsViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    val settingsDataStore = koinInject<SettingsDataStore>()
    val currencySymbol by settingsDataStore.currencySymbol.collectAsState(initial = "$")
    val material = state.materials.firstOrNull { it.id == materialId }

    if (material == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val typeColor = materialTypeColor(material.type)
    val totalCost = material.pricePerUnit * material.quantity

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(typeColor.copy(alpha = 0.1f)).padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(16.dp)).background(typeColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Inventory, null, tint = typeColor, modifier = Modifier.size(32.dp))
                }
                Text(material.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("${material.type.uppercase()} • ${material.unit}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Material Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    DetailRow("Type", material.type.replaceFirstChar { it.uppercase() })
                    DetailRow("Unit", material.unit)
                    DetailRow("Quantity", "${"%.2f".format(material.quantity)} ${material.unit}")
                    if (material.notes.isNotEmpty()) DetailRow("Notes", material.notes)
                }
            }

            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Cost Information", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    DetailRow("Price per Unit", if (material.pricePerUnit > 0) "$currencySymbol${"%.2f".format(material.pricePerUnit)}" else "Not set", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    if (material.quantity > 0 && material.pricePerUnit > 0) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Cost", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("$currencySymbol${"%.2f".format(totalCost)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate(Screen.AddMaterialPrice.createRoute(material.id)) },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Edit, null); Spacer(Modifier.width(8.dp)); Text("Update Price", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = color.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = color)
    }
}
