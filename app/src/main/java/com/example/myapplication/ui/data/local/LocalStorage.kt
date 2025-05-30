package com.example.myapplication.ui.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")
class LocalStorage (val context: Context){
    val tokenKey = stringPreferencesKey("token_key")
    val tokenFlow: Flow<String> = context.datastore.data.map{ preferences ->
        preferences[tokenKey] ?: ""
    }
    suspend fun setToken(token: String) {
        context.datastore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }
}