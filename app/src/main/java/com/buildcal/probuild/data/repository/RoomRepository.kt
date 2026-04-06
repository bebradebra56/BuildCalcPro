package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.RoomDao
import com.buildcal.probuild.data.db.entity.RoomEntity
import kotlinx.coroutines.flow.Flow

class RoomRepository(private val dao: RoomDao) {
    fun getRoomsByProject(projectId: Long): Flow<List<RoomEntity>> = dao.getRoomsByProject(projectId)
    fun getRoomCountForProject(projectId: Long): Flow<Int> = dao.getRoomCountForProject(projectId)
    suspend fun getRoomById(id: Long): RoomEntity? = dao.getRoomById(id)
    suspend fun insertRoom(room: RoomEntity): Long = dao.insertRoom(room)
    suspend fun updateRoom(room: RoomEntity) = dao.updateRoom(room)
    suspend fun deleteRoom(room: RoomEntity) = dao.deleteRoom(room)
}
