package com.buildcal.probuild.ui.screens.projects

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
import org.koin.androidx.compose.koinViewModel

private val buildingTypes = listOf(
    "Residential House", "Apartment", "Office Building",
    "Warehouse", "Garage", "Commercial", "Industrial", "Other"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(navController: NavController) {
    val vm: ProjectsViewModel = koinViewModel()
    var name by remember { mutableStateOf("") }
    var buildingType by remember { mutableStateOf(buildingTypes[0]) }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }

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
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Project Details", style = MaterialTheme.typography.titleMedium)

                InputField(
                    value = name,
                    onValueChange = { name = it; nameError = false },
                    label = "Project Name",
                    isError = nameError
                )
                if (nameError) {
                    Text("Project name is required", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }

                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = buildingType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Building Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        buildingTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = { buildingType = type; expanded = false }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
            }
        }

        Button(
            onClick = {
                if (name.isBlank()) {
                    nameError = true
                } else {
                    vm.addProject(name.trim(), buildingType, description.trim())
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Create Project", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
