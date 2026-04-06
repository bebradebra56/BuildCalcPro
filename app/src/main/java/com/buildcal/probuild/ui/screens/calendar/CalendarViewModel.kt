package com.buildcal.probuild.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.TaskEntity
import com.buildcal.probuild.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = LocalDate.now(),
    val allTasks: List<TaskEntity> = emptyList()
)

class CalendarViewModel(private val taskRepo: TaskRepository) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDate = MutableStateFlow<LocalDate?>(LocalDate.now())

    val uiState: StateFlow<CalendarUiState> = combine(
        _currentMonth,
        _selectedDate,
        taskRepo.getAllTasks()
    ) { month, selected, tasks ->
        CalendarUiState(currentMonth = month, selectedDate = selected, allTasks = tasks)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CalendarUiState())

    fun selectDate(date: LocalDate) { _selectedDate.value = date }

    fun previousMonth() { _currentMonth.value = _currentMonth.value.minusMonths(1) }

    fun nextMonth() { _currentMonth.value = _currentMonth.value.plusMonths(1) }

    fun getTasksForDate(tasks: List<TaskEntity>, date: LocalDate): List<TaskEntity> {
        return tasks.filter {
            val taskDate = java.time.Instant.ofEpochMilli(it.dueDate).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            taskDate == date
        }
    }

    fun getDatesWithTasks(tasks: List<TaskEntity>, month: YearMonth): Set<LocalDate> {
        return tasks.mapNotNull {
            val d = java.time.Instant.ofEpochMilli(it.dueDate).atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            if (d.year == month.year && d.monthValue == month.monthValue) d else null
        }.toSet()
    }
}
