package com.example.myapplication.response

import android.app.Application
import com.example.myapplication.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NetworkApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NetworkApplication)
            modules(appModules)
        }
    }
}