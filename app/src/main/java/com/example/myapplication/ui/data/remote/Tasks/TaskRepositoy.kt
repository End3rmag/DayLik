package com.example.myapplication.ui.data.remote.Tasks

import TaskDao


class TaskRepository(private val dao: TaskDao) {
    suspend fun getTasks(): List<TaskEntity> = dao.getAll()
    suspend fun insert(task: TaskEntity) = dao.insert(task)
    suspend fun delete(task: TaskEntity) = dao.delete(task)
}