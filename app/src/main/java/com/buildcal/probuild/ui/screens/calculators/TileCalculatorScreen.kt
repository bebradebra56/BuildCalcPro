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
import com.buildcal.probuild.domain.calculator.TileCalculator
import com.buildcal.probuild.ui.components.*
import com.buildcal.probuild.ui.theme.TileColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun TileCalculatorScreen(navController: NavController) {
    val vm: CalculatorsViewModel = koinViewModel()
    var floorArea by remember { mutableStateOf("") }
    var tileWidth by remember { mutableStateOf("300") }
    var tileHeight by remember { mutableStateOf("300") }
    var groutWidth by remember { mutableStateOf("3") }
    var wastePct by remember { mutableStateOf("10") }
    var result by remember { mutableStateOf<com.buildcal.probuild.domain.calculator.TileResult?>(null) }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalculatorIconBadge(icon = Icons.Filled.GridOn, color = TileColor)

        SectionCard {
            Text("Area & Tile Size", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            NumberInputField(value = floorArea, onValueChange = { floorArea = it; saved = false }, label = "Floor/Wall Area", suffix = "m²")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = tileWidth, onValueChange = { tileWidth = it; saved = false }, label = "Tile Width", suffix = "mm", modifier = Modifier.weight(1f))
                NumberInputField(value = tileHeight, onValueChange = { tileHeight = it; saved = false }, label = "Tile Height", suffix = "mm", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = groutWidth, onValueChange = { groutWidth = it; saved = false }, label = "Grout Joint", suffix = "mm", modifier = Modifier.weight(1f))
                NumberInputField(value = wastePct, onValueChange = { wastePct = it; saved = false }, label = "Waste", suffix = "%", modifier = Modifier.weight(1f))
            }
        }

        Button(
            onClick = {
                val area = floorArea.toDoubleOrNull() ?: return@Button
                val tw = tileWidth.toDoubleOrNull() ?: 300.0
                val th = tileHeight.toDoubleOrNull() ?: 300.0
                val gw = groutWidth.toDoubleOrNull() ?: 3.0
                val waste = 1.0 + (wastePct.toDoubleOrNull() ?: 10.0) / 100.0
                result = TileCalculator.calculate(area, tw, th, gw, waste)
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TileColor)
        ) {
            Icon(Icons.Filled.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate", modifier = Modifier.padding(vertical = 4.dp))
        }

        result?.let { r ->
            ResultCard {
                Text("Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                ResultRow("Tiles Needed", "${r.tilesNeeded} pcs")
                ResultRow("With Waste (${wastePct}%)", "${r.tilesWithWaste} pcs", highlight = true)
                ResultRow("Grout (approx)", "${"%.1f".format(r.groutKg)} kg")
                ResultRow("Tile Adhesive", "${"%.1f".format(r.adhesiveKg)} kg")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        if (!saved) {
                            vm.saveCalculation("tile", "Tiles for ${floorArea} m²", "area=$floorArea,tw=$tileWidth,th=$tileHeight", "count=${r.tilesNeeded},waste=${r.tilesWithWaste}")
                            saved = true
                        }
                    },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(if (saved) Icons.Filled.Check else Icons.Filled.Save, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp)); Text(if (saved) "Saved" else "Save")
                }
                OutlinedButton(onClick = { result = null; floorArea = "" }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Reset")
                }
            }
        }
    }
}
