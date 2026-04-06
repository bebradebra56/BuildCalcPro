package com.buildcal.probuild.ui.screens.measurements

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.db.entity.MeasurementEntity
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.SwipeToDeleteBox
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MeasurementsScreen(navController: NavController) {
    val vm: MeasurementsViewModel = koinViewModel()
    val measurements by vm.measurements.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (measurements.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyStateMessage(icon = Icons.Filled.Straighten, title = "No measurements saved", subtitle = "Tap + to save a measurement")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(measurements, key = { it.id }) { m ->
                    SwipeToDeleteBox(onDelete = { vm.deleteMeasurement(m) }) {
                        MeasurementCard(m)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddMeasurement.route) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Measurement", tint = Color.White)
        }
    }
}

@Composable
private fun MeasurementCard(m: MeasurementEntity) {
    val area = m.length * m.width
    val perimeter = 2 * (m.length + m.width)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier.size(46.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Straighten, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(m.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${m.length} × ${m.width} ${m.unit}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Area: ${"%.2f".format(area)} ${m.unit}²", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("P: ${"%.2f".format(perimeter)} ${m.unit}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (m.notes.isNotEmpty()) Text(m.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
