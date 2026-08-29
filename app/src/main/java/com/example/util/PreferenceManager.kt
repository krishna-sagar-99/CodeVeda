package com.example.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferenceManager(private val context: Context) {
    private val themeKey = booleanPreferencesKey("is_dark_mode")
    private val languageKey = stringPreferencesKey("app_language")

    val isDarkMode: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[themeKey]
    }

    val appLanguage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[languageKey] ?: "English"
    }

    suspend fun setTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = isDark
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }
}
