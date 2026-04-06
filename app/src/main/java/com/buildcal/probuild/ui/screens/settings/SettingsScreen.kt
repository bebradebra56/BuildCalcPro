package com.buildcal.probuild.ui.screens.settings

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val vm: SettingsViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    var currencyExpanded by remember { mutableStateOf(false) }
    var wasteSlider by remember(state.wasteFactor) { mutableFloatStateOf(((state.wasteFactor - 1f) * 100f).coerceIn(0f, 30f)) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingSection(title = "Units") {
            Text("Measurement System", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("metric" to "Metric (m, kg)", "imperial" to "Imperial (ft, lb)").forEach { (value, label) ->
                    FilterChip(
                        selected = state.units == value,
                        onClick = { vm.setUnits(value) },
                        label = { Text(label) }
                    )
                }
            }
        }

        SettingSection(title = "Currency") {
            ExposedDropdownMenuBox(expanded = currencyExpanded, onExpandedChange = { currencyExpanded = it }) {
                OutlinedTextField(
                    value = "${state.currencySymbol} ${state.currency}",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Currency") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = currencyExpanded, onDismissRequest = { currencyExpanded = false }) {
                    currencies.forEach { (code, symbol, name) ->
                        DropdownMenuItem(
                            text = { Text("$symbol $code — $name") },
                            onClick = { vm.setCurrency(code, symbol); currencyExpanded = false }
                        )
                    }
                }
            }
        }

        SettingSection(title = "Default Waste Factor") {
            Text("Extra material for cutting and waste: ${"%.0f".format(wasteSlider)}%", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Slider(
                value = wasteSlider,
                onValueChange = { wasteSlider = it },
                onValueChangeFinished = { vm.setWasteFactor(1f + wasteSlider / 100f) },
                valueRange = 0f..30f,
                steps = 29
            )
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("0%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("30%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        SettingSection(title = "About") {
            SettingInfoRow(Icons.Filled.Info, "Version", "1.0.0")
            SettingInfoRow(Icons.Filled.Build, "Build", "2026.04")
        }
    }
}

@Composable
private fun SettingSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun SettingInfoRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
