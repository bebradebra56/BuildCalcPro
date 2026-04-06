package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.MeasurementDao
import com.buildcal.probuild.data.db.entity.MeasurementEntity
import kotlinx.coroutines.flow.Flow

class MeasurementRepository(private val dao: MeasurementDao) {
    fun getAllMeasurements(): Flow<List<MeasurementEntity>> = dao.getAllMeasurements()
    fun getMeasurementsByProject(projectId: Long): Flow<List<MeasurementEntity>> = dao.getMeasurementsByProject(projectId)
    suspend fun getMeasurementById(id: Long): MeasurementEntity? = dao.getMeasurementById(id)
    suspend fun insertMeasurement(measurement: MeasurementEntity): Long = dao.insertMeasurement(measurement)
    suspend fun updateMeasurement(measurement: MeasurementEntity) = dao.updateMeasurement(measurement)
    suspend fun deleteMeasurement(measurement: MeasurementEntity) = dao.deleteMeasurement(measurement)
}
