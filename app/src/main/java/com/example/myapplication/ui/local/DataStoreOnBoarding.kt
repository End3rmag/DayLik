package com.example.myapplication.ui.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.myapplication.ui.data.local.datastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreOnBoarding(private val context: Context){
    private val onBoardingKey = booleanPreferencesKey("onboarding")
    private val localStorage = context.datastore

    suspend fun setOnBoardingCompleted(completed: Boolean){
        localStorage.edit { settings ->
            settings[onBoardingKey] = completed
        }
    }

    val onBoardingCompleted: Flow<Boolean> = localStorage.data
        .map { preferences -> preferences[onBoardingKey] ?: false
        }
}