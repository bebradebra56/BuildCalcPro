package com.buildcal.probuild.data.db.dao

import androidx.room.*
import com.buildcal.probuild.data.db.entity.CalculationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationDao {
    @Query("SELECT * FROM calculations ORDER BY createdAt DESC")
    fun getAllCalculations(): Flow<List<CalculationEntity>>

    @Query("SELECT * FROM calculations ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentCalculations(limit: Int = 10): Flow<List<CalculationEntity>>

    @Query("SELECT * FROM calculations WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getCalculationsByProject(projectId: Long): Flow<List<CalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: CalculationEntity): Long

    @Delete
    suspend fun deleteCalculation(calculation: CalculationEntity)

    @Query("DELETE FROM calculations WHERE id = :id")
    suspend fun deleteCalculationById(id: Long)

    @Query("SELECT COUNT(*) FROM calculations")
    fun getCalculationCount(): Flow<Int>
}
