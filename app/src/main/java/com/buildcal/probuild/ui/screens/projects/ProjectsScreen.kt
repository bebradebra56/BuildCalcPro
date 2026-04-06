package com.buildcal.probuild.ui.screens.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.db.entity.ProjectEntity
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.SwipeToDeleteBox
import com.buildcal.probuild.ui.navigation.Screen
import com.buildcal.probuild.ui.theme.GradientEnd
import com.buildcal.probuild.ui.theme.GradientStart
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectsScreen(navController: NavController) {
    val vm: ProjectsViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(GradientStart, GradientEnd)))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    "Projects",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (state.projects.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyStateMessage(
                        icon = Icons.Filled.FolderOpen,
                        title = "No projects yet",
                        subtitle = "Tap + to create your first project"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.projects, key = { it.id }) { project ->
                        SwipeToDeleteBox(onDelete = { vm.deleteProject(project) }) {
                            ProjectCard(
                                project = project,
                                roomCountFlow = vm.getRoomCount(project.id),
                                onClick = { navController.navigate(Screen.ProjectDetail.createRoute(project.id)) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddProject.route) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Project", tint = Color.White)
        }
    }
}

@Composable
private fun ProjectCard(
    project: ProjectEntity,
    roomCountFlow: kotlinx.coroutines.flow.Flow<Int>,
    onClick: () -> Unit
) {
    val roomCount by roomCountFlow.collectAsState(initial = 0)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Apartment, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(project.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(project.buildingType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.MeetingRoom, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$roomCount rooms", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        SimpleDateFormat("MMM d", Locale.ENGLISH).format(Date(project.createdAt)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
