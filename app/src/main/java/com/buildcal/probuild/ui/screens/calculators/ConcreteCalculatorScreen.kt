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
import com.buildcal.probuild.domain.calculator.ConcreteCalculator
import com.buildcal.probuild.ui.components.*
import com.buildcal.probuild.ui.theme.ConcreteColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConcreteCalculatorScreen(navController: NavController) {
    val vm: CalculatorsViewModel = koinViewModel()
    var length by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var wastePct by remember { mutableStateOf("5") }
    var result by remember { mutableStateOf<com.buildcal.probuild.domain.calculator.ConcreteResult?>(null) }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalculatorIconBadge(icon = Icons.Filled.Straighten, color = ConcreteColor)

        SectionCard {
            Text("Dimensions", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = length, onValueChange = { length = it; saved = false }, label = "Length", suffix = "m", modifier = Modifier.weight(1f))
                NumberInputField(value = width, onValueChange = { width = it; saved = false }, label = "Width", suffix = "m", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = height, onValueChange = { height = it; saved = false }, label = "Thickness/Height", suffix = "m", modifier = Modifier.weight(1f))
                NumberInputField(value = wastePct, onValueChange = { wastePct = it; saved = false }, label = "Waste", suffix = "%", modifier = Modifier.weight(1f))
            }
        }

        Button(
            onClick = {
                val l = length.toDoubleOrNull() ?: return@Button
                val w = width.toDoubleOrNull() ?: return@Button
                val h = height.toDoubleOrNull() ?: return@Button
                val waste = 1.0 + (wastePct.toDoubleOrNull() ?: 5.0) / 100.0
                result = ConcreteCalculator.calculate(l, w, h, waste)
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ConcreteColor)
        ) {
            Icon(Icons.Filled.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate", modifier = Modifier.padding(vertical = 4.dp))
        }

        result?.let { r ->
            ResultCard {
                Text("Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                ResultRow("Concrete Volume", "${"%.3f".format(r.volume)} m³", highlight = true)
                ResultRow("Cement Bags (50kg)", "${r.cementBags} bags")
                ResultRow("Sand", "${"%.0f".format(r.sandKg)} kg")
                ResultRow("Aggregate (Gravel)", "${"%.0f".format(r.aggregateKg)} kg")
                ResultRow("Water (approx)", "${"%.0f".format(r.waterLiters)} L")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        if (!saved) {
                            vm.saveCalculation(
                                type = "concrete",
                                title = "Concrete ${length}m × ${width}m × ${height}m",
                                inputData = "l=$length,w=$width,h=$height",
                                resultData = "vol=${r.volume},cement=${r.cementBags},sand=${r.sandKg}"
                            )
                            saved = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(if (saved) Icons.Filled.Check else Icons.Filled.Save, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(if (saved) "Saved" else "Save")
                }
                OutlinedButton(
                    onClick = { result = null; length = ""; width = ""; height = "" },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Reset")
                }
            }
        }
    }
}
