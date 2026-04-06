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
import com.buildcal.probuild.domain.calculator.DrywallCalculator
import com.buildcal.probuild.ui.components.*
import com.buildcal.probuild.ui.theme.DrywallColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun DrywallCalculatorScreen(navController: NavController) {
    val vm: CalculatorsViewModel = koinViewModel()
    var wallArea by remember { mutableStateOf("") }
    var panelWidth by remember { mutableStateOf("1200") }
    var panelHeight by remember { mutableStateOf("2500") }
    var wastePct by remember { mutableStateOf("10") }
    var result by remember { mutableStateOf<com.buildcal.probuild.domain.calculator.DrywallResult?>(null) }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalculatorIconBadge(icon = Icons.Filled.TableChart, color = DrywallColor)

        SectionCard {
            Text("Drywall Parameters", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            NumberInputField(value = wallArea, onValueChange = { wallArea = it; saved = false }, label = "Wall Area", suffix = "m²")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = panelWidth, onValueChange = { panelWidth = it; saved = false }, label = "Panel Width", suffix = "mm", modifier = Modifier.weight(1f))
                NumberInputField(value = panelHeight, onValueChange = { panelHeight = it; saved = false }, label = "Panel Height", suffix = "mm", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            NumberInputField(value = wastePct, onValueChange = { wastePct = it; saved = false }, label = "Waste", suffix = "%")
        }

        Button(
            onClick = {
                val area = wallArea.toDoubleOrNull() ?: return@Button
                val pw = panelWidth.toDoubleOrNull() ?: 1200.0
                val ph = panelHeight.toDoubleOrNull() ?: 2500.0
                val waste = 1.0 + (wastePct.toDoubleOrNull() ?: 10.0) / 100.0
                result = DrywallCalculator.calculate(area, pw, ph, waste)
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DrywallColor)
        ) {
            Icon(Icons.Filled.Calculate, null); Spacer(Modifier.width(8.dp)); Text("Calculate", modifier = Modifier.padding(vertical = 4.dp))
        }

        result?.let { r ->
            ResultCard {
                Text("Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                ResultRow("Panels Needed", "${r.panelsNeeded} sheets")
                ResultRow("With Waste (${wastePct}%)", "${r.panelsWithWaste} sheets", highlight = true)
                ResultRow("Drywall Screws", "${r.screwsNeeded} pcs")
                ResultRow("Joint Compound", "${"%.1f".format(r.jointsCompound)} kg")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = {
                    if (!saved) { vm.saveCalculation("drywall", "Drywall ${wallArea} m²", "area=$wallArea,pw=$panelWidth,ph=$panelHeight", "panels=${r.panelsNeeded},waste=${r.panelsWithWaste}"); saved = true }
                }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(if (saved) Icons.Filled.Check else Icons.Filled.Save, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text(if (saved) "Saved" else "Save")
                }
                OutlinedButton(onClick = { result = null; wallArea = "" }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Reset")
                }
            }
        }
    }
}
