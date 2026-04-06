package com.buildcal.probuild.ui.screens.materials

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.components.InputField
import com.buildcal.probuild.ui.components.NumberInputField
import org.koin.androidx.compose.koinViewModel

private val materialTypes = listOf("brick", "concrete", "tile", "paint", "drywall", "insulation", "steel", "wood", "other")
private val unitOptions = listOf("pcs", "m²", "m³", "m", "kg", "L", "roll", "bag", "sheet", "ton")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMaterialScreen(navController: NavController) {
    val vm: MaterialsViewModel = koinViewModel()
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(materialTypes[0]) }
    var unit by remember { mutableStateOf(unitOptions[0]) }
    var pricePerUnit by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InputField(value = name, onValueChange = { name = it; nameError = false }, label = "Material Name", isError = nameError)

                ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = it }) {
                    OutlinedTextField(
                        value = type.replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Material Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                        materialTypes.forEach { t ->
                            DropdownMenuItem(text = { Text(t.replaceFirstChar { it.uppercase() }) }, onClick = { type = t; typeExpanded = false })
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberInputField(value = quantity, onValueChange = { quantity = it }, label = "Quantity", modifier = Modifier.weight(1f))
                    ExposedDropdownMenuBox(expanded = unitExpanded, onExpandedChange = { unitExpanded = it }, modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = unit, onValueChange = {}, readOnly = true,
                            label = { Text("Unit") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) {
                            unitOptions.forEach { u ->
                                DropdownMenuItem(text = { Text(u) }, onClick = { unit = u; unitExpanded = false })
                            }
                        }
                    }
                }

                NumberInputField(value = pricePerUnit, onValueChange = { pricePerUnit = it }, label = "Price per Unit", suffix = "per $unit")
                InputField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)")
            }
        }

        Button(
            onClick = {
                if (name.isBlank()) { nameError = true; return@Button }
                vm.addMaterial(name.trim(), type, unit, pricePerUnit.toDoubleOrNull() ?: 0.0, quantity.toDoubleOrNull() ?: 0.0, notes.trim())
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); Text("Add Material", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
