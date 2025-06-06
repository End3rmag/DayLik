package com.example.myapplication.ui.data.Worker
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.R
import com.example.myapplication.ui.data.local.repository.TaskRepository
import com.example.myapplication.ui.data.remote.Tasks.formatAsTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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
                "На сегодня у вас ${tasks.size} ${tasksCountText(tasks.size)}\n" +
                        "Зайдите в приложение чтобы узнать какие.\uD83E\uDD13"
            } else {
                "На сегодня задач нет, можно пить пиво!\uD83C\uDF7B"
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
            val name = "Ежедневные напоминания"
            val descriptionText = "Канал для ежедневных напоминаний о задачах"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
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

    override suspend fun doWork(): Result {
        try {
            Log.d("TaskReminder", "Worker started at ${System.currentTimeMillis()}")
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            Log.d("TaskReminder", "Current time: $now")
            val tasks = taskRepository.getTasks()
                .map { it.toTask() }
                .filter { task ->
                    task.notifyEnabled &&
                            task.time != null &&
                            task.date == now.date &&
                            isTimeWithinHour(task.time, now)
                }
            Log.d("TaskReminder", "Found ${tasks.size} tasks for reminder")

            tasks.forEach { task ->
                val notificationText = "Вы запланировали: ${task.title}\n" +
                        "На: ${task.time?.formatAsTime()}"
                sendNotification(
                    title = "Напоминание",
                    text = notificationText
                )
            }

            return Result.success()
        } catch (e: Exception) {
            Log.e("TaskReminderWorker", "Error in doWork", e)
            return Result.failure()
        }
    }

    private fun isTimeWithinHour(taskTime: String, now: LocalDateTime): Boolean {
        val formattedTime = if (taskTime.length == 4 && !taskTime.contains(":")) {
            "${taskTime.take(2)}:${taskTime.drop(2)}"
        } else {
            taskTime
        }

        val (hours, minutes) = try {
            formattedTime.split(":").map { it.toInt() }
        } catch (e: Exception) {
            Log.e("TaskReminder", "Error parsing time: $formattedTime", e)
            return false
        }

        val currentMinutes = now.hour * 60 + now.minute
        val taskMinutes = hours * 60 + minutes
        val notificationMinutes = taskMinutes - 60

        return currentMinutes in notificationMinutes..taskMinutes
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(title: String, text: String) {
        try {
            Log.d("TaskReminder", "Creating notification channel")
            createNotificationChannel()

            val notificationId = title.hashCode()
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.generated_image)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            Log.d("TaskReminder", "Building notification: title=$title, text=$text")

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
                Log.d("TaskReminder", "Notification displayed successfully, id=$notificationId")
            }
        } catch (e: Exception) {
            Log.e("TaskReminder", "Error sending notification", e)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Напоминания о задачах"
            val descriptionText = "Канал для напоминаний о задачах"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "task_reminders"
    }
}