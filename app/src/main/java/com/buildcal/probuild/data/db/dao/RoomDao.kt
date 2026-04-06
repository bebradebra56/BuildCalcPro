package com.buildcal.probuild.data.db.dao

import androidx.room.*
import com.buildcal.probuild.data.db.entity.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getRoomsByProject(projectId: Long): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getRoomById(id: Long): RoomEntity?

    @Query("SELECT COUNT(*) FROM rooms WHERE projectId = :projectId")
    fun getRoomCountForProject(projectId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity): Long

    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Delete
    suspend fun deleteRoom(room: RoomEntity)
}
