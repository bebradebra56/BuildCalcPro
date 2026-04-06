package com.buildcal.probuild.data.db.dao

import androidx.room.*
import com.buildcal.probuild.data.db.entity.MeasurementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Query("SELECT * FROM measurements ORDER BY createdAt DESC")
    fun getAllMeasurements(): Flow<List<MeasurementEntity>>

    @Query("SELECT * FROM measurements WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getMeasurementsByProject(projectId: Long): Flow<List<MeasurementEntity>>

    @Query("SELECT * FROM measurements WHERE id = :id")
    suspend fun getMeasurementById(id: Long): MeasurementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurement: MeasurementEntity): Long

    @Update
    suspend fun updateMeasurement(measurement: MeasurementEntity)

    @Delete
    suspend fun deleteMeasurement(measurement: MeasurementEntity)
}
