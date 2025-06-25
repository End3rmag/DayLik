package com.example.myapplication.ui.data.Worker
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.ui.data.local.repository.TaskRepository
import com.example.myapplication.ui.data.remote.Tasks.Task
import com.example.myapplication.ui.data.remote.Tasks.formatAsTime
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DailyNotificationsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val taskRepository: TaskRepository by inject()

    override suspend fun doWork(): Result {
        try {
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date

            val tasks = taskRepository.getTasks()
                .filter { it.toTask().date == today }

            val text = if (tasks.isNotEmpty()) {
                "На сегодня у вас  ${tasks.size} ${tasksCountText(tasks.size)}\n" +
                        "Зайдите в приложение чтобы узнать какие.\uD83E\uDD13"
            } else {
                "На сегодня задач нет."
            }

            sendNotification(text)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }

    private fun tasksCountText(count: Int): String {
        return when {
            count % 10 == 1 && count % 100 != 11 -> "задача"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "задачи"
            else -> "задач"
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(text: String) {
        createNotificationChannel()

        val notificationId = 1
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.generated_image)
            .setContentTitle("Ежедневное напоминание")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                NotificationManager.IMPORTANCE_HIGH
            } else {
                NotificationManager.IMPORTANCE_DEFAULT
            }

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Ежедневные напоминания",
                importance
            ).apply {
                description = "Канал для ежедневных напоминаний о задачах"

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 200, 500)
                    enableLights(true)
                    lightColor = Color.BLUE
                }
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    companion object {
        const val CHANNEL_ID = "daily_reminders"
    }
}

class TaskReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val taskRepository: TaskRepository by inject()
    private val pref = context.getSharedPreferences("task_reminders", Context.MODE_PRIVATE)

    override suspend fun doWork(): Result {
        try {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val currentDate = now.date
            val currentTimeInMinutes = now.hour * 60 + now.minute

            Log.d("TaskReminder", "Checking tasks at ${now.hour}:${now.minute}")

            val tasks = taskRepository.getTasks()
                .map { it.toTask() }
                .filter { task ->
                    task.notifyEnabled &&
                            task.time != null &&
                            task.date == currentDate
                }

            Log.d("TaskReminder", "Found ${tasks.size} tasks to check")

            tasks.forEach { task ->
                val taskTime = parseTime(task.time!!) ?: return@forEach
                val notificationTime = taskTime - 60 // За час до события
                val notificationKey = "task_${task.id}_${task.date}_${task.time}"

                Log.d("TaskReminder", "Task: ${task.title}, Time: $taskTime, NotifyAt: $notificationTime")

                if (currentTimeInMinutes in notificationTime until taskTime) {
                    if (!pref.getBoolean(notificationKey, false)) {
                        Log.d("TaskReminder", "Sending notification for: ${task.title}")
                        sendNotification(task)
                        pref.edit().putBoolean(notificationKey, true).apply()
                    }
                }
            }

            return Result.success()
        } catch (e: Exception) {
            Log.e("TaskReminder", "Error in worker", e)
            return Result.failure()
        }
    }

    private fun parseTime(timeStr: String): Int? {
        return try {
            val cleanTime = timeStr.replace(":", "").take(4).padStart(4, '0')
            val hours = cleanTime.take(2).toInt()
            val minutes = cleanTime.drop(2).take(2).toInt()
            hours * 60 + minutes
        } catch (e: Exception) {
            Log.e("TaskReminder", "Error parsing time: $timeStr", e)
            null
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(task: Task) {
        createNotificationChannel() // Дублирующая проверка на случай, если канал был удален

        val notificationText = "Вы запланировали: ${task.title}\n" +
                "На: ${task.time?.formatAsTime()}"

        val notificationId = task.id.hashCode()
        val builder = NotificationCompat.Builder(applicationContext, "task_reminders")
            .setSmallIcon(R.drawable.generated_image)
            .setContentTitle("Напоминание о задаче")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        with(NotificationManagerCompat.from(applicationContext)) {
            try {
                notify(notificationId, builder.build())
                Log.d("TaskReminder", "Notification sent successfully")
            } catch (e: Exception) {
                Log.e("TaskReminder", "Failed to send notification", e)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_reminders",
                "Срочные напоминания",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления за час до задачи"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}


class DayBeforeNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val taskRepository: TaskRepository by inject()
    private val pref = context.getSharedPreferences("day_before_notifications", Context.MODE_PRIVATE)

    override suspend fun doWork(): Result {
        try {
            val tomorrow = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.plus(1, DateTimeUnit.DAY)

            val tasks = taskRepository.getTasks()
                .map { it.toTask() }
                .filter { task ->
                    task.notifyDayBefore &&
                            task.date == tomorrow
                }
           tasks.forEach { task ->
                val notificationKey = "day_before_${task.id}_${task.date}"
                val alreadyNotified = pref.getBoolean(notificationKey, false)

               if (!alreadyNotified) {
                    sendNotification(
                        title = "Напоминание на завтра",
                        text = "Не забудьте, на завтра у вас запланировано: ${task.title}"
                    )
                    pref.edit().putBoolean(notificationKey, true).apply()
                }
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(title: String, text: String) {
        createNotificationChannel()

        val notificationId = title.hashCode()
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.generated_image)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Уведомления накануне",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о задачах на следующий день"
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "day_before_notifications"
    }
}