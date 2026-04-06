package com.buildcal.probuild.ui.screens.reports

import androidx.lifecycle.ViewModel
import com.buildcal.probuild.data.db.entity.CalculationEntity
import com.buildcal.probuild.data.db.entity.MaterialEntity
import com.buildcal.probuild.data.repository.CalculationRepository
import com.buildcal.probuild.data.repository.MaterialRepository
import com.buildcal.probuild.data.repository.ProjectRepository
import kotlinx.coroutines.flow.*

data class ReportsUiState(
    val calculations: List<CalculationEntity> = emptyList(),
    val materials: List<MaterialEntity> = emptyList(),
    val projectCount: Int = 0,
    val totalCost: Double = 0.0,
    val materialCountByType: Map<String, Int> = emptyMap()
)

class ReportsViewModel(
    calculationRepo: CalculationRepository,
    materialRepo: MaterialRepository,
    projectRepo: ProjectRepository
) : ViewModel() {

    val uiState: Flow<ReportsUiState> = combine(
        calculationRepo.getAllCalculations(),
        materialRepo.getAllMaterials(),
        projectRepo.getProjectCount(),
        materialRepo.getTotalEstimatedCost()
    ) { calcs, materials, projCount, cost ->
        ReportsUiState(
            calculations = calcs,
            materials = materials,
            projectCount = projCount,
            totalCost = cost ?: 0.0,
            materialCountByType = materials.groupBy { it.type }.mapValues { it.value.size }
        )
    }
}
