package com.example.myapplication.ui.data.local.repository

import com.example.myapplication.ui.data.local.TaskDao
import com.example.myapplication.ui.data.remote.Tasks.TaskEntity


class TaskRepository(private val dao: TaskDao) {
    suspend fun getTasks(): List<TaskEntity> = dao.getAll()
    suspend fun getTaskById(id: String): TaskEntity? = dao.getById(id)
    suspend fun getTasksByOriginalId(originalId: String): List<TaskEntity> = dao.getByOriginalId(originalId)
    suspend fun insert(task: TaskEntity) = dao.insert(task)
    suspend fun delete(task: TaskEntity) = dao.delete(task)
    suspend fun update(task: TaskEntity) = dao.update(task)
    suspend fun deleteAllByOriginalId(originalId: String) = dao.deleteAllByOriginalId(originalId)
}
