package com.buildcal.probuild.ui.screens.calculators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildcal.probuild.data.db.entity.CalculationEntity
import com.buildcal.probuild.data.repository.CalculationRepository
import kotlinx.coroutines.launch

class CalculatorsViewModel(
    private val calculationRepo: CalculationRepository
) : ViewModel() {

    fun saveCalculation(type: String, title: String, inputData: String, resultData: String) {
        viewModelScope.launch {
            calculationRepo.insertCalculation(
                CalculationEntity(
                    type = type,
                    title = title,
                    inputData = inputData,
                    resultData = resultData
                )
            )
        }
    }
}
