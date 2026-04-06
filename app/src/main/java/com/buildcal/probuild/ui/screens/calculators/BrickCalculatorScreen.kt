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
import com.buildcal.probuild.domain.calculator.BrickCalculator
import com.buildcal.probuild.ui.components.*
import com.buildcal.probuild.ui.theme.BrickColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun BrickCalculatorScreen(navController: NavController) {
    val vm: CalculatorsViewModel = koinViewModel()
    var wallHeight by remember { mutableStateOf("") }
    var wallWidth by remember { mutableStateOf("") }
    var brickLength by remember { mutableStateOf("250") }
    var brickHeight by remember { mutableStateOf("65") }
    var mortarThickness by remember { mutableStateOf("10") }
    var wasteFactor by remember { mutableStateOf("5") }
    var result by remember { mutableStateOf<com.buildcal.probuild.domain.calculator.BrickResult?>(null) }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalculatorIconBadge(icon = Icons.Filled.ViewModule, color = BrickColor)

        SectionCard {
            Text("Wall Dimensions", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = wallHeight, onValueChange = { wallHeight = it; saved = false }, label = "Height", suffix = "m", modifier = Modifier.weight(1f))
                NumberInputField(value = wallWidth, onValueChange = { wallWidth = it; saved = false }, label = "Width", suffix = "m", modifier = Modifier.weight(1f))
            }
        }

        SectionCard {
            Text("Brick Size (Standard)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = brickLength, onValueChange = { brickLength = it; saved = false }, label = "Length", suffix = "mm", modifier = Modifier.weight(1f))
                NumberInputField(value = brickHeight, onValueChange = { brickHeight = it; saved = false }, label = "Height", suffix = "mm", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = mortarThickness, onValueChange = { mortarThickness = it; saved = false }, label = "Mortar", suffix = "mm", modifier = Modifier.weight(1f))
                NumberInputField(value = wasteFactor, onValueChange = { wasteFactor = it; saved = false }, label = "Waste", suffix = "%", modifier = Modifier.weight(1f))
            }
        }

        Button(
            onClick = {
                val h = wallHeight.toDoubleOrNull() ?: return@Button
                val w = wallWidth.toDoubleOrNull() ?: return@Button
                val bl = brickLength.toDoubleOrNull() ?: 250.0
                val bh = brickHeight.toDoubleOrNull() ?: 65.0
                val m = mortarThickness.toDoubleOrNull() ?: 10.0
                val waste = 1.0 + (wasteFactor.toDoubleOrNull() ?: 5.0) / 100.0
                result = BrickCalculator.calculate(h, w, bl, bh, m, waste)
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrickColor)
        ) {
            Icon(Icons.Filled.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate", modifier = Modifier.padding(vertical = 4.dp))
        }

        result?.let { r ->
            ResultCard {
                Text("Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                ResultRow("Wall Area", "${"%.2f".format(r.wallArea)} m²")
                ResultRow("Bricks Needed", "${r.bricksNeeded} pcs")
                ResultRow("With Waste (${wasteFactor}%)", "${r.bricksWithWaste} pcs", highlight = true)
                ResultRow("Mortar (approx)", "${"%.3f".format(r.mortar)} m³")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        if (!saved) {
                            vm.saveCalculation(
                                type = "brick",
                                title = "Brick Wall ${wallHeight}m × ${wallWidth}m",
                                inputData = "h=$wallHeight,w=$wallWidth,brickL=$brickLength,brickH=$brickHeight",
                                resultData = "area=${r.wallArea},count=${r.bricksNeeded},withWaste=${r.bricksWithWaste}"
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
                    onClick = { result = null; wallHeight = ""; wallWidth = "" },
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
