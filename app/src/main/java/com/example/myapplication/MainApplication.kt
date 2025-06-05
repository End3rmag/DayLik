package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.myapplication.di.appModules
import com.example.myapplication.ui.data.Worker.NotificationsManagement

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

        if (!notificationWorkIsScheduled()) {
            val notificationsManagement = NotificationsManagement(applicationContext)
            notificationsManagement.setupAllNotifications()
        }
    }
    private fun notificationWorkIsScheduled(): Boolean {
        // Здесь можно добавить логику проверки, установлены ли уже работы
        return false
    }
}