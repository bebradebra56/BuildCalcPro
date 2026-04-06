package com.buildcal.probuild.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.buildcal.probuild.data.db.entity.ProjectEntity
import com.buildcal.probuild.data.db.entity.TaskEntity
import com.buildcal.probuild.data.repository.*
import kotlinx.coroutines.flow.*

data class DashboardUiState(
    val recentProjects: List<ProjectEntity> = emptyList(),
    val projectCount: Int = 0,
    val materialCount: Int = 0,
    val totalCost: Double = 0.0,
    val pendingTasks: List<TaskEntity> = emptyList(),
    val shoppingPending: Int = 0
)

class DashboardViewModel(
    projectRepo: ProjectRepository,
    materialRepo: MaterialRepository,
    taskRepo: TaskRepository,
    shoppingRepo: ShoppingRepository,
    calculationRepo: CalculationRepository
) : ViewModel() {

    private val projectsFlow = combine(
        projectRepo.getRecentProjects(5),
        projectRepo.getProjectCount()
    ) { projects, count -> Pair(projects, count) }

    private val materialsFlow = combine(
        materialRepo.getMaterialCount(),
        materialRepo.getTotalEstimatedCost()
    ) { count, cost -> Pair(count, cost ?: 0.0) }

    private val tasksFlow = combine(
        taskRepo.getAllTasks(),
        shoppingRepo.getPendingItemCount()
    ) { tasks, shopping -> Pair(tasks, shopping) }

    val uiState: Flow<DashboardUiState> = combine(
        projectsFlow,
        materialsFlow,
        tasksFlow
    ) { (projects, projCount), (matCount, cost), (tasks, shopping) ->
        DashboardUiState(
            recentProjects = projects,
            projectCount = projCount,
            materialCount = matCount,
            totalCost = cost,
            pendingTasks = tasks.filter { !it.isCompleted }.take(3),
            shoppingPending = shopping
        )
    }
}
