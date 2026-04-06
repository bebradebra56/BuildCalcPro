package com.buildcal.probuild.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.buildcal.probuild.ui.components.PriorityChip
import com.buildcal.probuild.ui.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CalendarScreen(navController: NavController) {
    val vm: CalendarViewModel = koinViewModel()
    val state by vm.uiState.collectAsState()

    val datesWithTasks = remember(state.allTasks, state.currentMonth) {
        vm.getDatesWithTasks(state.allTasks, state.currentMonth)
    }
    val selectedTasks = remember(state.allTasks, state.selectedDate) {
        state.selectedDate?.let { vm.getTasksForDate(state.allTasks, it) } ?: emptyList()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { vm.previousMonth() }) {
                            Icon(Icons.Filled.ChevronLeft, "Previous month")
                        }
                        Text(
                            state.currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { vm.nextMonth() }) {
                            Icon(Icons.Filled.ChevronRight, "Next month")
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                            Text(day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(Modifier.height(8.dp))

                    val firstDayOfMonth = state.currentMonth.atDay(1)
                    val startOffset = firstDayOfMonth.dayOfWeek.value % 7
                    val daysInMonth = state.currentMonth.lengthOfMonth()
                    val today = LocalDate.now()

                    val weeks = ((startOffset + daysInMonth + 6) / 7)
                    for (week in 0 until weeks) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            for (dayOfWeek in 0..6) {
                                val dayNum = week * 7 + dayOfWeek - startOffset + 1
                                Box(modifier = Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                                    if (dayNum in 1..daysInMonth) {
                                        val date = state.currentMonth.atDay(dayNum)
                                        val isSelected = state.selectedDate == date
                                        val isToday = date == today
                                        val hasTask = date in datesWithTasks

                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isSelected -> MaterialTheme.colorScheme.primary
                                                        isToday -> MaterialTheme.colorScheme.primaryContainer
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .clickable { vm.selectDate(date) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    dayNum.toString(),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = when {
                                                        isSelected -> MaterialTheme.colorScheme.onPrimary
                                                        isToday -> MaterialTheme.colorScheme.primary
                                                        else -> MaterialTheme.colorScheme.onSurface
                                                    }
                                                )
                                                if (hasTask) {
                                                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    state.selectedDate?.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)) ?: "Select a date",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                FilledTonalButton(onClick = { navController.navigate(Screen.AddTask.route) }, shape = RoundedCornerShape(10.dp)) {
                    Icon(Icons.Filled.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add Task")
                }
            }
        }

        if (selectedTasks.isEmpty()) {
            item {
                Text("No tasks for this day", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
            }
        } else {
            items(selectedTasks) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(if (task.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(task.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            if (task.description.isNotEmpty()) Text(task.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        PriorityChip(task.priority)
                    }
                }
            }
        }
    }
}
