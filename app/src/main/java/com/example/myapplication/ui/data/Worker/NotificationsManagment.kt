package com.example.myapplication.ui.data.Worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationsManagement(
    private val context: Context
) {
    fun setupAllNotifications() {
        setupDailyNotification()
        setupTaskReminders()
        setupDayBeforeNotifications()
    }

    private fun setupDailyNotification() {
        val dailyRequest = PeriodicWorkRequestBuilder<DailyNotificationsWorker>(
            24, TimeUnit.HOURS
        ).setInitialDelay(
            calculateInitialDelay(9, 0),
            TimeUnit.MILLISECONDS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_notification_work",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyRequest
        )
    }

    private fun setupTaskReminders() {
        val reminderRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(
            15, TimeUnit.MINUTES
        ).setInitialDelay(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "task_reminder_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            reminderRequest
        )
    }

    private fun setupDayBeforeNotifications() {
        val dayBeforeRequest = PeriodicWorkRequestBuilder<DayBeforeNotificationWorker>(
            24, TimeUnit.HOURS // Временно для теста
        ).setInitialDelay(
            calculateInitialDelay(18, 0), // Установите ближайшее время
            TimeUnit.MILLISECONDS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "day_before_notification_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            dayBeforeRequest
        )
    }

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= now) add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis - now
    }
}