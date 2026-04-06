package com.buildcal.probuild.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SettingsUiState(
    val units: String = "metric",
    val currency: String = "USD",
    val currencySymbol: String = "$",
    val wasteFactor: Float = 1.10f
)

val currencies = listOf(
    Triple("USD", "$", "US Dollar"),
    Triple("EUR", "€", "Euro"),
    Triple("GBP", "£", "British Pound"),
    Triple("JPY", "¥", "Japanese Yen"),
    Triple("CAD", "C$", "Canadian Dollar"),
    Triple("AUD", "A$", "Australian Dollar"),
    Triple("RUB", "₽", "Russian Ruble"),
    Triple("CNY", "¥", "Chinese Yuan"),
)

class SettingsViewModel(private val settings: SettingsDataStore) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settings.units, settings.currency, settings.currencySymbol, settings.wasteFactor
    ) { units, currency, symbol, waste ->
        SettingsUiState(units = units, currency = currency, currencySymbol = symbol, wasteFactor = waste)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setUnits(units: String) { viewModelScope.launch { settings.setUnits(units) } }
    fun setCurrency(currency: String, symbol: String) { viewModelScope.launch { settings.setCurrency(currency, symbol) } }
    fun setWasteFactor(factor: Float) { viewModelScope.launch { settings.setWasteFactor(factor) } }
}
