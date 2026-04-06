package com.buildcal.probuild.ui.screens.estimates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.components.NumberInputField
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddMaterialPriceScreen(navController: NavController, materialId: Long) {
    val vm: EstimatesViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    val material = state.materials.firstOrNull { it.id == materialId }
    var price by remember { mutableStateOf(material?.pricePerUnit?.let { if (it > 0) "%.2f".format(it) else "" } ?: "") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (material != null) {
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), elevation = CardDefaults.cardElevation(2.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(material.name, style = MaterialTheme.typography.titleMedium)
                    Text("${material.type} • ${material.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Set Price", style = MaterialTheme.typography.titleSmall)
                NumberInputField(
                    value = price,
                    onValueChange = { price = it; error = false },
                    label = "Price per ${material?.unit ?: "unit"}",
                    suffix = state.currencySymbol,
                    isError = error
                )
                if (error) Text("Enter a valid price", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
        }

        Button(
            onClick = {
                val p = price.toDoubleOrNull()
                if (p == null || p < 0) { error = true; return@Button }
                vm.updateMaterialPrice(materialId, p)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Save, null); Spacer(Modifier.width(8.dp)); Text("Save Price", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
