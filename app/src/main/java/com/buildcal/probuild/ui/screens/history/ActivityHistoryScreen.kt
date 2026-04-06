package com.buildcal.probuild.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.repository.CalculationRepository
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.screens.materials.materialTypeColor
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ActivityHistoryScreen(navController: NavController) {
    val calcRepo = koinInject<CalculationRepository>()
    val calculations by calcRepo.getAllCalculations().collectAsState(initial = emptyList())

    if (calculations.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EmptyStateMessage(icon = Icons.Filled.History, title = "No activity yet", subtitle = "Your calculation history will appear here")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Text("${calculations.size} calculations", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(calculations) { calc ->
                val typeColor = materialTypeColor(calc.type)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(typeColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Calculate, null, tint = typeColor, modifier = Modifier.size(22.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(calc.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text(calc.type.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(calc.resultData.take(80), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            SimpleDateFormat("MMM d\nhh:mm a", Locale.ENGLISH).format(Date(calc.createdAt)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
