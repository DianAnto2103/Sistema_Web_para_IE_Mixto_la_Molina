package com.example.mixtotrackmobile.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("mixto_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_ROL = "rol"
        private const val KEY_TOKEN = "token"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_GRADO = "grado"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }


    fun guardarSesion(email: String, rol: UserRole, token: String, nombre: String?, grado: String?) {
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_ROL, rol.name)
            putString(KEY_TOKEN, token)
            putString(KEY_NOMBRE, nombre ?: email.split("@").first())
            putString(KEY_GRADO, grado ?: "Sin grado asignado")
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun guardarRol(rol: UserRole) {
        prefs.edit().putString(KEY_ROL, rol.name).apply()
    }

    fun getRol(): UserRole? {
        val rolName = prefs.getString(KEY_ROL, null)
        return rolName?.let { UserRole.valueOf(it) }
    }

    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun getNombre(): String? = prefs.getString(KEY_NOMBRE, null)
    fun getGrado(): String? = prefs.getString(KEY_GRADO, null)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}