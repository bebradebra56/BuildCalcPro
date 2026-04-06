package com.buildcal.probuild.ui.screens.tasks

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
import com.buildcal.probuild.ui.components.InputField
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController) {
    val vm: TasksViewModel = koinViewModel()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(1) }
    var dueDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InputField(value = title, onValueChange = { title = it; titleError = false }, label = "Task Title", isError = titleError)
                if (titleError) Text("Title is required", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)

                OutlinedTextField(
                    value = description, onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(), minLines = 2, maxLines = 4
                )

                OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.CalendarToday, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Due: ${dueDate.format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.ENGLISH))}")
                }

                Text("Priority", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1 to "Low", 2 to "Medium", 3 to "High").forEach { (p, label) ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = when (p) {
                                    3 -> MaterialTheme.colorScheme.errorContainer
                                    2 -> MaterialTheme.colorScheme.secondaryContainer
                                    else -> MaterialTheme.colorScheme.tertiaryContainer
                                }
                            )
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                if (title.isBlank()) { titleError = true; return@Button }
                val dueDateMillis = dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                vm.addTask(title.trim(), description.trim(), dueDateMillis, priority)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Add, null); Spacer(Modifier.width(8.dp)); Text("Create Task", modifier = Modifier.padding(vertical = 4.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        dueDate = java.time.Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
