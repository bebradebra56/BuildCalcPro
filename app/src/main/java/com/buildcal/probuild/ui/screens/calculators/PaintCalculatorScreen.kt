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
import com.buildcal.probuild.domain.calculator.PaintCalculator
import com.buildcal.probuild.ui.components.*
import com.buildcal.probuild.ui.theme.PaintColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun PaintCalculatorScreen(navController: NavController) {
    val vm: CalculatorsViewModel = koinViewModel()
    var wallArea by remember { mutableStateOf("") }
    var coverage by remember { mutableStateOf("10") }
    var coats by remember { mutableStateOf("2") }
    var canSize by remember { mutableStateOf("4") }
    var result by remember { mutableStateOf<com.buildcal.probuild.domain.calculator.PaintResult?>(null) }
    var saved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalculatorIconBadge(icon = Icons.Filled.FormatPaint, color = PaintColor)

        SectionCard {
            Text("Paint Parameters", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            NumberInputField(value = wallArea, onValueChange = { wallArea = it; saved = false }, label = "Wall Area", suffix = "m²")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberInputField(value = coverage, onValueChange = { coverage = it; saved = false }, label = "Coverage/L", suffix = "m²/L", modifier = Modifier.weight(1f))
                NumberInputField(value = coats, onValueChange = { coats = it; saved = false }, label = "Coats", modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            NumberInputField(value = canSize, onValueChange = { canSize = it; saved = false }, label = "Can Size", suffix = "L")
        }

        Button(
            onClick = {
                val area = wallArea.toDoubleOrNull() ?: return@Button
                val cov = coverage.toDoubleOrNull() ?: 10.0
                val c = coats.toIntOrNull() ?: 2
                val can = canSize.toDoubleOrNull() ?: 4.0
                result = PaintCalculator.calculate(area, cov, c, can)
                saved = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PaintColor)
        ) {
            Icon(Icons.Filled.Calculate, null)
            Spacer(Modifier.width(8.dp))
            Text("Calculate", modifier = Modifier.padding(vertical = 4.dp))
        }

        result?.let { r ->
            ResultCard {
                Text("Results", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                ResultRow("Paint Required", "${"%.2f".format(r.litersNeeded)} L")
                ResultRow("With 10% Waste", "${"%.2f".format(r.litersWithWaste)} L")
                ResultRow("Cans (${canSize}L each)", "${r.cansNeeded} cans", highlight = true)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        if (!saved) {
                            vm.saveCalculation("paint", "Paint for ${wallArea} m²", "area=$wallArea,cov=$coverage,coats=$coats", "liters=${r.litersNeeded},cans=${r.cansNeeded}")
                            saved = true
                        }
                    },
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(if (saved) Icons.Filled.Check else Icons.Filled.Save, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp)); Text(if (saved) "Saved" else "Save")
                }
                OutlinedButton(onClick = { result = null; wallArea = "" }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Reset")
                }
            }
        }
    }
}
