package com.buildcal.probuild.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.buildcal.probuild.data.repository.TaskRepository
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.PriorityChip
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationsScreen(navController: NavController) {
    val taskRepo = koinInject<TaskRepository>()
    val now = System.currentTimeMillis()
    val sevenDays = now + 7L * 24 * 60 * 60 * 1000
    val upcomingTasks by taskRepo.getTasksInRange(now, sevenDays).collectAsState(initial = emptyList())
    val overdueTasks by taskRepo.getTasksInRange(0L, now - 1).collectAsState(initial = emptyList())
    val pendingOverdue = overdueTasks.filter { !it.isCompleted }
    val pendingUpcoming = upcomingTasks.filter { !it.isCompleted }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (pendingOverdue.isEmpty() && pendingUpcoming.isEmpty()) {
            item {
                Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyStateMessage(icon = Icons.Filled.NotificationsNone, title = "All clear!", subtitle = "No upcoming or overdue tasks")
                }
            }
        } else {
            if (pendingOverdue.isNotEmpty()) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.error))
                        Text("Overdue (${pendingOverdue.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                    }
                }
                items(pendingOverdue) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Filled.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(22.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(task.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onErrorContainer)
                                Text("Was due: ${SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(task.dueDate))}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f))
                            }
                            PriorityChip(task.priority)
                        }
                    }
                }
            }

            if (pendingUpcoming.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                        Text("Upcoming (${pendingUpcoming.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
                items(pendingUpcoming) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Filled.CalendarToday, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(task.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                                Text("Due: ${SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(task.dueDate))}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            PriorityChip(task.priority)
                        }
                    }
                }
            }
        }
    }
}
