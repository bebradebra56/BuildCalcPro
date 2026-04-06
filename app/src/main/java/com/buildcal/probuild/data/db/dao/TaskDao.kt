package com.buildcal.probuild.data.db.dao

import androidx.room.*
import com.buildcal.probuild.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY dueDate ASC")
    fun getTasksByProject(projectId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :from AND :to ORDER BY dueDate ASC")
    fun getTasksInRange(from: Long, to: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun setTaskCompleted(id: Long, completed: Boolean)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    fun getPendingTaskCount(): Flow<Int>
}
