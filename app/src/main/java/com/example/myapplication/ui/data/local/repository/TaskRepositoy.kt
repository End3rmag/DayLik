package com.example.myapplication.ui.data.local.repository

import com.example.myapplication.ui.data.local.TaskDao
import com.example.myapplication.ui.data.remote.Tasks.TaskEntity


class TaskRepository(private val dao: TaskDao) {
    suspend fun getTasks(): List<TaskEntity> = dao.getAll()
    suspend fun insert(task: TaskEntity) = dao.insert(task)
    suspend fun delete(task: TaskEntity) = dao.delete(task)
    suspend fun update(task: TaskEntity) = dao.update(task)
}