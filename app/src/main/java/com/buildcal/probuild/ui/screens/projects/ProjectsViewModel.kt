package com.buildcal.probuild.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.ProjectEntity
import com.buildcal.probuild.data.repository.ProjectRepository
import com.buildcal.probuild.data.repository.RoomRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val projects: List<ProjectEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProjectsViewModel(
    private val projectRepo: ProjectRepository,
    private val roomRepo: RoomRepository
) : ViewModel() {

    val uiState: StateFlow<ProjectsUiState> = projectRepo.getAllProjects()
        .map { ProjectsUiState(projects = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectsUiState(isLoading = true))

    fun addProject(name: String, buildingType: String, description: String = "") {
        viewModelScope.launch {
            projectRepo.insertProject(
                ProjectEntity(name = name, buildingType = buildingType, description = description)
            )
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch { projectRepo.deleteProject(project) }
    }

    fun getRoomCount(projectId: Long): Flow<Int> = roomRepo.getRoomCountForProject(projectId)
}
