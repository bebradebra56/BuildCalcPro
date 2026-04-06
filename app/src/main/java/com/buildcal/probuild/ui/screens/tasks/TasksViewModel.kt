package com.buildcal.probuild.ui.screens.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.TaskEntity
import com.buildcal.probuild.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TasksViewModel(private val repo: TaskRepository) : ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = repo.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTask(title: String, description: String, dueDate: Long, priority: Int) {
        viewModelScope.launch {
            repo.insertTask(TaskEntity(title = title, description = description, dueDate = dueDate, priority = priority))
        }
    }

    fun toggleCompleted(task: TaskEntity) {
        viewModelScope.launch { repo.setTaskCompleted(task.id, !task.isCompleted) }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { repo.deleteTask(task) }
    }
}
