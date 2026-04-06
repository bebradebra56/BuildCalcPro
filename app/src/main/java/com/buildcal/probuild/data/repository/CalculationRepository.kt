package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.CalculationDao
import com.buildcal.probuild.data.db.entity.CalculationEntity
import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val dao: CalculationDao) {
    fun getAllCalculations(): Flow<List<CalculationEntity>> = dao.getAllCalculations()
    fun getRecentCalculations(limit: Int = 10): Flow<List<CalculationEntity>> = dao.getRecentCalculations(limit)
    fun getCalculationsByProject(projectId: Long): Flow<List<CalculationEntity>> = dao.getCalculationsByProject(projectId)
    fun getCalculationCount(): Flow<Int> = dao.getCalculationCount()
    suspend fun insertCalculation(calculation: CalculationEntity): Long = dao.insertCalculation(calculation)
    suspend fun deleteCalculation(calculation: CalculationEntity) = dao.deleteCalculation(calculation)
    suspend fun deleteCalculationById(id: Long) = dao.deleteCalculationById(id)
}
