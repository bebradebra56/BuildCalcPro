package com.buildcal.probuild.ui.screens.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.RoomEntity
import com.buildcal.probuild.data.repository.RoomRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RoomsUiState(
    val rooms: List<RoomEntity> = emptyList(),
    val isLoading: Boolean = false
)

class RoomsViewModel(
    private val repo: RoomRepository,
    private val projectId: Long
) : ViewModel() {

    val uiState: StateFlow<RoomsUiState> = repo.getRoomsByProject(projectId)
        .map { RoomsUiState(rooms = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RoomsUiState(isLoading = true))

    fun addRoom(name: String, length: Double, width: Double, height: Double) {
        viewModelScope.launch {
            repo.insertRoom(RoomEntity(projectId = projectId, name = name, length = length, width = width, height = height))
        }
    }

    fun deleteRoom(room: RoomEntity) {
        viewModelScope.launch { repo.deleteRoom(room) }
    }
}
