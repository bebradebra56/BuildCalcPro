package com.buildcal.probuild.ui.screens.calculators

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
import com.buildcal.probuild.domain.calculator.InsulationCalculator
import com.buildcal.probuild.ui.components.*
import com.buildcal.probuild.ui.theme.InsulationColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun InsulationCalculatorScreen(navController: NavController) {
    val vm: CalculatorsViewModel = koinViewModel()
    var surfaceArea by remember { mutableStateOf("") }
    var rollWidth by remember { mutableStateOf("0.6") }
    var rollLength by remember { mutableStateOf("10") }
    var wastePct by remember { mutableStateOf("10") }
    var result by remember { mutableStateOf<com.buildcal.probuild.domain.calculator.InsulationResult?>(null) }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalculatorIconBadge(icon = Icons.Filled.Layers, color = InsulationColor)

        SectionCard {
            Text("Insulation Parameters", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            NumberInputField(value = surfaceArea, onValueChange = { surfaceArea = it; saved = false }, label = "Surface Area", suffix = "m²")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = rollWidth, onValueChange = { rollWidth = it; saved = false }, label = "Roll Width", suffix = "m", modifier = Modifier.weight(1f))
                NumberInputField(value = rollLength, onValueChange = { rollLength = it; saved = false }, label = "Roll Length", suffix = "m", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            NumberInputField(value = wastePct, onValueChange = { wastePct = it; saved = false }, label = "Waste", suffix = "%")
        }

        Button(
            onClick = {
                val area = surfaceArea.toDoubleOrNull() ?: return@Button
                val rw = rollWidth.toDoubleOrNull() ?: 0.6
                val rl = rollLength.toDoubleOrNull() ?: 10.0
                val waste = 1.0 + (wastePct.toDoubleOrNull() ?: 10.0) / 100.0
                result = InsulationCalculator.calculate(area, rw, rl, waste)
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = InsulationColor)
        ) {
            Icon(Icons.Filled.Calculate, null); Spacer(Modifier.width(8.dp)); Text("Calculate", modifier = Modifier.padding(vertical = 4.dp))
        }

        result?.let { r ->
            ResultCard {
                Text("Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                ResultRow("Rolls Needed", "${r.rollsNeeded} rolls")
                ResultRow("With Waste (${wastePct}%)", "${r.rollsWithWaste} rolls", highlight = true)
                ResultRow("Total Coverage", "${"%.1f".format(r.totalCoverage)} m²")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = {
                    if (!saved) { vm.saveCalculation("insulation", "Insulation ${surfaceArea} m²", "area=$surfaceArea,rw=$rollWidth,rl=$rollLength", "rolls=${r.rollsNeeded},waste=${r.rollsWithWaste}"); saved = true }
                }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(if (saved) Icons.Filled.Check else Icons.Filled.Save, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text(if (saved) "Saved" else "Save")
                }
                OutlinedButton(onClick = { result = null; surfaceArea = "" }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Reset")
                }
            }
        }
    }
}
