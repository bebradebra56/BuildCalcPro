package com.buildcal.probuild.data.repository

import com.buildcal.probuild.data.db.dao.ProjectDao
import com.buildcal.probuild.data.db.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val dao: ProjectDao) {
    fun getAllProjects(): Flow<List<ProjectEntity>> = dao.getAllProjects()
    fun getRecentProjects(limit: Int = 5): Flow<List<ProjectEntity>> = dao.getRecentProjects(limit)
    fun getProjectCount(): Flow<Int> = dao.getProjectCount()
    suspend fun getProjectById(id: Long): ProjectEntity? = dao.getProjectById(id)
    suspend fun insertProject(project: ProjectEntity): Long = dao.insertProject(project)
    suspend fun updateProject(project: ProjectEntity) = dao.updateProject(project)
    suspend fun deleteProject(project: ProjectEntity) = dao.deleteProject(project)
}
