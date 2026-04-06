package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.TaskDao
import com.buildcal.probuild.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    fun getAllTasks(): Flow<List<TaskEntity>> = dao.getAllTasks()
    fun getTasksByProject(projectId: Long): Flow<List<TaskEntity>> = dao.getTasksByProject(projectId)
    fun getTasksInRange(from: Long, to: Long): Flow<List<TaskEntity>> = dao.getTasksInRange(from, to)
    fun getPendingTaskCount(): Flow<Int> = dao.getPendingTaskCount()
    suspend fun getTaskById(id: Long): TaskEntity? = dao.getTaskById(id)
    suspend fun insertTask(task: TaskEntity): Long = dao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = dao.updateTask(task)
    suspend fun deleteTask(task: TaskEntity) = dao.deleteTask(task)
    suspend fun setTaskCompleted(id: Long, completed: Boolean) = dao.setTaskCompleted(id, completed)
}
