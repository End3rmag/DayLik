package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.myapplication.di.appModules
import com.example.myapplication.ui.data.Worker.NotificationsManagement
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModules)
        }

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
        WorkManager.initialize(this, config)

        clearOldNotifications(this)

            val notificationsManagement = NotificationsManagement(applicationContext)
            notificationsManagement.setupAllNotifications()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            checkLegacyNotificationSettings()
        }

    }
    fun clearOldNotifications(context: Context) {
        val prefs = context.getSharedPreferences("task_reminders", Context.MODE_PRIVATE)
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toString()
        prefs.all.keys.forEach { key ->
            if (key.contains("task_") && !key.contains(today)) {
                prefs.edit().remove(key).apply()
            }
        }
    }
    private fun checkLegacyNotificationSettings() {
        val prefs = getSharedPreferences("notification_prefs", MODE_PRIVATE)
        if (!prefs.getBoolean("legacy_settings_checked", false)) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = notificationManager.getNotificationChannel("task_reminders")
                if (channel?.importance == NotificationManager.IMPORTANCE_NONE) {
                    // Канал отключен, показываем подсказку
                    showNotificationSettingsHint()
                }
            }

            prefs.edit().putBoolean("legacy_settings_checked", true).apply()
        }
    }
    private fun showNotificationSettingsHint() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Уведомления отключены")
            .setMessage("Для правильной работы приложения необходимо включить уведомления. Хотите перейти в настройки?")
            .setPositiveButton("Настройки") { _, _ ->
                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                    }
                } else {
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:$packageName")
                    }
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("Позже", null)
            .create()
        alertDialog.show()
    }
}
