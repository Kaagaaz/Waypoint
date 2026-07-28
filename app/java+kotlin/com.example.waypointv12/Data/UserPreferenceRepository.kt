package com.example.waypointv12.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

enum class AppTheme { LIGHT, DARK, SYSTEM }

class UserPreferencesRepository private constructor(private val context: Context) {
    private val AUTO_RESOLVE_KEY = booleanPreferencesKey("auto_resolve")
    private val THEME_KEY = stringPreferencesKey("app_theme")

    val autoResolveFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTO_RESOLVE_KEY] ?: false
        }

    val themeFlow: Flow<AppTheme> = context.dataStore.data
        .map { preferences ->
            val themeName = preferences[THEME_KEY] ?: AppTheme.DARK.name
            try { AppTheme.valueOf(themeName) } catch (e: Exception) { AppTheme.DARK }
        }

    suspend fun setAutoResolve(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_RESOLVE_KEY] = enabled
        }
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferencesRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
