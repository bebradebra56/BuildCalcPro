package com.buildcal.probuild.ui.screens.materials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.MaterialEntity
import com.buildcal.probuild.data.repository.MaterialRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MaterialsUiState(
    val materials: List<MaterialEntity> = emptyList(),
    val totalCost: Double = 0.0,
    val isLoading: Boolean = false
)

class MaterialsViewModel(private val repo: MaterialRepository) : ViewModel() {

    val uiState: StateFlow<MaterialsUiState> = combine(
        repo.getAllMaterials(),
        repo.getTotalEstimatedCost()
    ) { materials, cost ->
        MaterialsUiState(materials = materials, totalCost = cost ?: 0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaterialsUiState(isLoading = true))

    fun addMaterial(name: String, type: String, unit: String, pricePerUnit: Double, quantity: Double, notes: String = "") {
        viewModelScope.launch {
            repo.insertMaterial(
                MaterialEntity(name = name, type = type, unit = unit, pricePerUnit = pricePerUnit, quantity = quantity, notes = notes)
            )
        }
    }

    fun updatePrice(material: MaterialEntity, price: Double) {
        viewModelScope.launch {
            repo.updateMaterial(material.copy(pricePerUnit = price))
        }
    }

    fun deleteMaterial(material: MaterialEntity) {
        viewModelScope.launch { repo.deleteMaterial(material) }
    }

    suspend fun getMaterialById(id: Long): MaterialEntity? = repo.getMaterialById(id)
}
