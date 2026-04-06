package com.buildcal.probuild.ui.screens.measurements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.MeasurementEntity
import com.buildcal.probuild.data.repository.MeasurementRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MeasurementsViewModel(private val repo: MeasurementRepository) : ViewModel() {

    val measurements: StateFlow<List<MeasurementEntity>> = repo.getAllMeasurements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMeasurement(name: String, length: Double, width: Double, height: Double, unit: String, notes: String) {
        viewModelScope.launch {
            repo.insertMeasurement(MeasurementEntity(name = name, length = length, width = width, height = height, unit = unit, notes = notes))
        }
    }

    fun deleteMeasurement(measurement: MeasurementEntity) {
        viewModelScope.launch { repo.deleteMeasurement(measurement) }
    }
}
