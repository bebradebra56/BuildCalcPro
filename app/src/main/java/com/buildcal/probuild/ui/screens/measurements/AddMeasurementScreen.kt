package com.buildcal.probuild.ui.screens.measurements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.components.InputField
import com.buildcal.probuild.ui.components.NumberInputField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeasurementScreen(navController: NavController) {
    val vm: MeasurementsViewModel = koinViewModel()
    var name by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("m") }
    var notes by remember { mutableStateOf("") }
    var unitExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    val unitOptions = listOf("m", "cm", "mm", "ft", "in")
    val area = (length.toDoubleOrNull() ?: 0.0) * (width.toDoubleOrNull() ?: 0.0)

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InputField(value = name, onValueChange = { name = it; nameError = false }, label = "Measurement Name", isError = nameError)

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberInputField(value = length, onValueChange = { length = it }, label = "Length", suffix = unit, modifier = Modifier.weight(1f))
                    NumberInputField(value = width, onValueChange = { width = it }, label = "Width", suffix = unit, modifier = Modifier.weight(1f))
                }
                NumberInputField(value = height, onValueChange = { height = it }, label = "Height (optional)", suffix = unit)

                ExposedDropdownMenuBox(expanded = unitExpanded, onExpandedChange = { unitExpanded = it }) {
                    OutlinedTextField(
                        value = unit, onValueChange = {}, readOnly = true, label = { Text("Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) {
                        unitOptions.forEach { u -> DropdownMenuItem(text = { Text(u) }, onClick = { unit = u; unitExpanded = false }) }
                    }
                }
                InputField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)")

                if (area > 0) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer), shape = RoundedCornerShape(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column {
                                Text("Area", style = MaterialTheme.typography.labelSmall)
                                Text("${"%.3f".format(area)} $unit²", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Perimeter", style = MaterialTheme.typography.labelSmall)
                                Text("${"%.3f".format(2 * ((length.toDoubleOrNull() ?: 0.0) + (width.toDoubleOrNull() ?: 0.0)))} $unit", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (name.isBlank()) { nameError = true; return@Button }
                val l = length.toDoubleOrNull() ?: 0.0
                val w = width.toDoubleOrNull() ?: 0.0
                val h = height.toDoubleOrNull() ?: 0.0
                vm.addMeasurement(name.trim(), l, w, h, unit, notes.trim())
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Save, null); Spacer(Modifier.width(8.dp)); Text("Save Measurement", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
