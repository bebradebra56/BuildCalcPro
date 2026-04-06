package com.buildcal.probuild.ui.screens.rooms

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
import com.buildcal.probuild.ui.components.NumberInputField
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private val roomPresets = listOf("Living Room", "Kitchen", "Bedroom", "Bathroom", "Hallway", "Garage", "Basement", "Office", "Dining Room", "Custom")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddRoomScreen(navController: NavController, projectId: Long) {
    val vm: RoomsViewModel = koinViewModel(parameters = { parametersOf(projectId) })
    var name by remember { mutableStateOf("") }
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("2.7") }
    var nameError by remember { mutableStateOf(false) }
    var dimensionError by remember { mutableStateOf(false) }

    val area = (length.toDoubleOrNull() ?: 0.0) * (width.toDoubleOrNull() ?: 0.0)
    val volume = area * (height.toDoubleOrNull() ?: 0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Room Name", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    roomPresets.dropLast(1).forEach { preset ->
                        FilterChip(
                            selected = name == preset,
                            onClick = { name = preset; nameError = false },
                            label = { Text(preset) }
                        )
                    }
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = { Text("Room Name") },
                    isError = nameError,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Dimensions", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    NumberInputField(value = length, onValueChange = { length = it; dimensionError = false }, label = "Length", suffix = "m", modifier = Modifier.weight(1f), isError = dimensionError)
                    NumberInputField(value = width, onValueChange = { width = it; dimensionError = false }, label = "Width", suffix = "m", modifier = Modifier.weight(1f), isError = dimensionError)
                }
                NumberInputField(value = height, onValueChange = { height = it }, label = "Height", suffix = "m")

                if (area > 0) {
                    Divider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Floor Area", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${"%.2f".format(area)} m²", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("Volume", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${"%.2f".format(volume)} m³", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                val l = length.toDoubleOrNull()
                val w = width.toDoubleOrNull()
                val h = height.toDoubleOrNull() ?: 2.7
                when {
                    name.isBlank() -> nameError = true
                    l == null || l <= 0 || w == null || w <= 0 -> dimensionError = true
                    else -> {
                        vm.addRoom(name.trim(), l, w, h)
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add Room", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
