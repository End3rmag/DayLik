package com.example.myapplication.ui.data.Worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.myapplication.ui.data.local.repository.TaskRepository

class DailyNotificationsWorkerFactory(
    private val taskRepository: TaskRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            DailyNotificationsWorker::class.java.name ->
                DailyNotificationsWorker(appContext, workerParameters)
            TaskReminderWorker::class.java.name ->
                TaskReminderWorker(appContext, workerParameters)
            else -> null
        }
    }
}
