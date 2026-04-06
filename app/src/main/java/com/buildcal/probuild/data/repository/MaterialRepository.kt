package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.MaterialDao
import com.buildcal.probuild.data.db.entity.MaterialEntity
import kotlinx.coroutines.flow.Flow

class MaterialRepository(private val dao: MaterialDao) {
    fun getAllMaterials(): Flow<List<MaterialEntity>> = dao.getAllMaterials()
    fun getMaterialsByProject(projectId: Long): Flow<List<MaterialEntity>> = dao.getMaterialsByProject(projectId)
    fun getTotalEstimatedCost(): Flow<Double?> = dao.getTotalEstimatedCost()
    fun getMaterialCount(): Flow<Int> = dao.getMaterialCount()
    suspend fun getMaterialById(id: Long): MaterialEntity? = dao.getMaterialById(id)
    suspend fun insertMaterial(material: MaterialEntity): Long = dao.insertMaterial(material)
    suspend fun updateMaterial(material: MaterialEntity) = dao.updateMaterial(material)
    suspend fun deleteMaterial(material: MaterialEntity) = dao.deleteMaterial(material)
}
