package com.buildcal.probuild.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.datastore.SettingsDataStore
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.navigation.Screen
import com.buildcal.probuild.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.*

private data class QuickCalc(val label: String, val route: String, val icon: ImageVector, val color: Color)

private val quickCalcs = listOf(
    QuickCalc("Brick", Screen.BrickCalculator.route, Icons.Filled.ViewModule, BrickColor),
    QuickCalc("Concrete", Screen.ConcreteCalculator.route, Icons.Filled.Straighten, ConcreteColor),
    QuickCalc("Tile", Screen.TileCalculator.route, Icons.Filled.GridOn, TileColor),
    QuickCalc("Paint", Screen.PaintCalculator.route, Icons.Filled.FormatPaint, PaintColor),
    QuickCalc("Drywall", Screen.DrywallCalculator.route, Icons.Filled.TableChart, DrywallColor),
    QuickCalc("Insulation", Screen.InsulationCalculator.route, Icons.Filled.Layers, InsulationColor)
)

@Composable
fun DashboardScreen(navController: NavController) {
    val vm: DashboardViewModel = koinViewModel()
    val settingsDataStore = koinInject<SettingsDataStore>()
    val state by vm.uiState.collectAsState(initial = DashboardUiState())
    val currencySymbol by settingsDataStore.currencySymbol.collectAsState(initial = "$")
    val profileName by settingsDataStore.profileName.collectAsState(initial = "")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        if (profileName.isNotEmpty()) "Hello, $profileName" else "Welcome back",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        "Build Calc Pro",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).format(Date()),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Folder,
                        value = state.projectCount.toString(),
                        label = "Projects",
                        color = MaterialTheme.colorScheme.primary
                    )
                    StatMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.Inventory,
                        value = state.materialCount.toString(),
                        label = "Materials",
                        color = MaterialTheme.colorScheme.secondary
                    )
                    StatMiniCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Filled.ShoppingCart,
                        value = state.shoppingPending.toString(),
                        label = "To Buy",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                Spacer(Modifier.height(24.dp))
                Text(
                    "Quick Calculators",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quickCalcs) { calc ->
                    QuickCalcCard(
                        label = calc.label,
                        icon = calc.icon,
                        color = calc.color,
                        onClick = { navController.navigate(calc.route) }
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Projects", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = { navController.navigate(Screen.Projects.route) }) { Text("See all") }
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        if (state.recentProjects.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    EmptyStateMessage(
                        icon = Icons.Filled.Folder,
                        title = "No projects yet",
                        subtitle = "Tap Projects to create your first project"
                    )
                }
            }
        } else {
            items(state.recentProjects) { project ->
                RecentProjectCard(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    name = project.name,
                    type = project.buildingType,
                    onClick = { navController.navigate(Screen.ProjectDetail.createRoute(project.id)) }
                )
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Upcoming Tasks", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = { navController.navigate(Screen.Tasks.route) }) { Text("See all") }
                }
                Spacer(Modifier.height(8.dp))
                if (state.pendingTasks.isEmpty()) {
                    Text(
                        "No upcoming tasks",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    state.pendingTasks.forEach { task ->
                        TaskSummaryItem(
                            title = task.title,
                            dueDate = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(task.dueDate)),
                            priority = task.priority
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Estimated Cost", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Budget", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text(
                                "$currencySymbol${"%.2f".format(state.totalCost)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMiniCard(modifier: Modifier = Modifier, icon: ImageVector, value: String, label: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QuickCalcCard(label: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun RecentProjectCard(modifier: Modifier = Modifier, name: String, type: String, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Apartment, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(type, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun TaskSummaryItem(title: String, dueDate: String, priority: Int) {
    val priorityColor = when (priority) {
        3 -> MaterialTheme.colorScheme.error
        2 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.tertiary
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.size(6.dp, 6.dp).clip(RoundedCornerShape(3.dp)).background(priorityColor))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(dueDate, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
        }
    }
}
