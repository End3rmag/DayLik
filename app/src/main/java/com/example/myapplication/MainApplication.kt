package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.myapplication.di.appModules
import com.example.myapplication.ui.data.Worker.NotificationsManagement
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
}
