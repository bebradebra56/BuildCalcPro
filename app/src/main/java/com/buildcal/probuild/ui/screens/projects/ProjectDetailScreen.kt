package com.buildcal.probuild.ui.screens.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectDetailScreen(navController: NavController, projectId: Long) {
    val vm: ProjectsViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()
    val roomCount by vm.getRoomCount(projectId).collectAsState(initial = 0)
    val project = state.projects.firstOrNull { it.id == projectId }

    if (project == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Apartment, null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(project.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(project.buildingType, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                    if (project.description.isNotEmpty()) {
                        Text(project.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    }
                    Text(
                        "Created: ${SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(Date(project.createdAt))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.MeetingRoom,
                    value = roomCount.toString(),
                    label = "Rooms"
                )
                StatBox(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Build,
                    value = "–",
                    label = "Materials"
                )
            }
        }

        item {
            Spacer(Modifier.height(20.dp))
            Text(
                "Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(12.dp))
        }

        val actions = listOf(
            Triple(Icons.Filled.MeetingRoom, "Rooms", Screen.Rooms.createRoute(projectId)),
            Triple(Icons.Filled.Calculate, "Calculators", Screen.Calculators.route),
            Triple(Icons.Filled.Inventory, "Materials", Screen.Materials.route),
            Triple(Icons.Filled.ShoppingCart, "Shopping List", Screen.Shopping.route),
            Triple(Icons.Filled.Straighten, "Measurements", Screen.Measurements.route),
            Triple(Icons.Filled.Assessment, "Reports", Screen.Reports.route)
        )

        actions.forEach { (icon, title, route) ->
            item {
                ActionCard(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    icon = icon,
                    title = title,
                    onClick = { navController.navigate(route) }
                )
            }
        }
    }
}

@Composable
private fun StatBox(modifier: Modifier = Modifier, icon: ImageVector, value: String, label: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ActionCard(modifier: Modifier = Modifier, icon: ImageVector, title: String, onClick: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            }
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
