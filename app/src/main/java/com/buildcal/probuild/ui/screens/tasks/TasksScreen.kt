package com.buildcal.probuild.ui.screens.tasks

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.data.db.entity.TaskEntity
import com.buildcal.probuild.ui.components.EmptyStateMessage
import com.buildcal.probuild.ui.components.PriorityChip
import com.buildcal.probuild.ui.components.SwipeToDeleteBox
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TasksScreen(navController: NavController) {
    val vm: TasksViewModel = koinViewModel()
    val tasks by vm.tasks.collectAsState()
    val pending = tasks.filter { !it.isCompleted }
    val completed = tasks.filter { it.isCompleted }

    Box(modifier = Modifier.fillMaxSize()) {
        if (tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                EmptyStateMessage(icon = Icons.Filled.Task, title = "No tasks yet", subtitle = "Tap + to create a task")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (pending.isNotEmpty()) {
                    item { Text("Pending (${pending.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp)) }
                    items(pending, key = { it.id }) { task ->
                        SwipeToDeleteBox(onDelete = { vm.deleteTask(task) }) {
                            TaskCard(task = task, onToggle = { vm.toggleCompleted(task) })
                        }
                    }
                }
                if (completed.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Text("Completed (${completed.size})", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 4.dp))
                    }
                    items(completed, key = { it.id }) { task ->
                        SwipeToDeleteBox(onDelete = { vm.deleteTask(task) }) {
                            TaskCard(task = task, onToggle = { vm.toggleCompleted(task) })
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.AddTask.route) },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Task", tint = Color.White)
        }
    }
}

@Composable
private fun TaskCard(task: TaskEntity, onToggle: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(if (task.isCompleted) 0.dp else 2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    task.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                if (task.description.isNotEmpty()) {
                    Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    "Due: ${SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(Date(task.dueDate))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            PriorityChip(task.priority)
        }
    }
}
