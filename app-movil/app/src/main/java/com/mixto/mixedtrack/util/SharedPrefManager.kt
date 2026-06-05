package com.mixto.mixedtrack.util

import android.content.Context

class SharedPrefManager(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "mixto_track_prefs"
        private const val KEY_TOKEN = "key_access_token"
    }

    fun saveToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    fun clearSession() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }
}
