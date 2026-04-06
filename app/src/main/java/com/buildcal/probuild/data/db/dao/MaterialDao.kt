package com.buildcal.probuild.data.db.dao

import androidx.room.*
import com.buildcal.probuild.data.db.entity.MaterialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materials ORDER BY createdAt DESC")
    fun getAllMaterials(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getMaterialsByProject(projectId: Long): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE id = :id")
    suspend fun getMaterialById(id: Long): MaterialEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: MaterialEntity): Long

    @Update
    suspend fun updateMaterial(material: MaterialEntity)

    @Delete
    suspend fun deleteMaterial(material: MaterialEntity)

    @Query("SELECT SUM(pricePerUnit * quantity) FROM materials")
    fun getTotalEstimatedCost(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM materials")
    fun getMaterialCount(): Flow<Int>
}
