package com.buildcal.probuild.ui.screens.estimates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.MaterialEntity
import com.buildcal.probuild.data.repository.MaterialRepository
import com.buildcal.probuild.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class EstimatesUiState(
    val materials: List<MaterialEntity> = emptyList(),
    val totalCost: Double = 0.0,
    val currencySymbol: String = "$"
)

class EstimatesViewModel(
    private val materialRepo: MaterialRepository,
    private val settings: SettingsDataStore
) : ViewModel() {

    val uiState: StateFlow<EstimatesUiState> = combine(
        materialRepo.getAllMaterials(),
        materialRepo.getTotalEstimatedCost(),
        settings.currencySymbol
    ) { materials, cost, symbol ->
        EstimatesUiState(
            materials = materials,
            totalCost = cost ?: 0.0,
            currencySymbol = symbol
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EstimatesUiState())

    fun updateMaterialPrice(materialId: Long, price: Double) {
        viewModelScope.launch {
            val material = materialRepo.getMaterialById(materialId) ?: return@launch
            materialRepo.updateMaterial(material.copy(pricePerUnit = price))
        }
    }
}
