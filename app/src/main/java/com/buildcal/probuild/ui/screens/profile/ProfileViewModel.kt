package com.buildcal.probuild.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val email: String = ""
)

class ProfileViewModel(private val settings: SettingsDataStore) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        settings.profileName, settings.profileEmail
    ) { name, email ->
        ProfileUiState(name = name, email = email)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileUiState())

    fun saveName(name: String) { viewModelScope.launch { settings.setProfileName(name) } }
    fun saveEmail(email: String) { viewModelScope.launch { settings.setProfileEmail(email) } }
}
