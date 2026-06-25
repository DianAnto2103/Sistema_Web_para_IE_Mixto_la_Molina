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
        private const val KEY_DEPARTAMENTO = "departamento"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_APELLIDO = "apellido"
        private const val KEY_DOCENTE_ID = "docente_id"
    }

    // ========== GUARDAR SESIÓN ==========
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

    fun guardarSesion(email: String, rol: UserRole, token: String, nombre: String?, grado: String?, userId: Int? = null) {
        prefs.edit().apply {
            putString(KEY_EMAIL, email)
            putString(KEY_ROL, rol.name)
            putString(KEY_TOKEN, token)
            putString(KEY_NOMBRE, nombre ?: email.split("@").first())
            putString(KEY_GRADO, grado ?: "Sin grado asignado")
            userId?.let { putInt(KEY_USER_ID, it) }
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun guardarDatosPerfil(nombre: String?, grado: String?, departamento: String?) {
        prefs.edit().apply {
            nombre?.let { putString(KEY_NOMBRE, it) }
            grado?.let { putString(KEY_GRADO, it) }
            departamento?.let { putString(KEY_DEPARTAMENTO, it) }
            apply()
        }
    }

    fun guardarRol(rol: UserRole) {
        prefs.edit().putString(KEY_ROL, rol.name).apply()
    }

    fun guardarUserId(userId: Int) {
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun guardarDocenteId(docenteId: Int) {
        prefs.edit().putInt(KEY_DOCENTE_ID, docenteId).apply()
    }

    // ========== GETTERS ==========
    fun getUserId(): Int? {
        return if (prefs.contains(KEY_USER_ID)) {
            prefs.getInt(KEY_USER_ID, 0).takeIf { it > 0 }
        } else null
    }

    fun getRol(): UserRole? {
        val rolName = prefs.getString(KEY_ROL, null)
        return rolName?.let { UserRole.valueOf(it) }
    }

    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun getNombre(): String? = prefs.getString(KEY_NOMBRE, null)
    fun getGrado(): String? = prefs.getString(KEY_GRADO, null)
    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun getDepartamento(): String? = prefs.getString(KEY_DEPARTAMENTO, null)

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }

    // ========== MÉTODOS PARA DOCENTE ==========

    /**
     * Obtiene el nombre completo del usuario
     * Para docente: "Juan Pérez"
     */
    fun getNombreCompleto(): String {
        val nombre = getNombre() ?: "Docente"
        val apellido = prefs.getString(KEY_APELLIDO, null)
        return if (apellido != null) {
            "$nombre $apellido"
        } else {
            nombre
        }
    }

    /**
     * ✅ UNIFICADO: Obtiene el ID del docente
     * Primero intenta desde KEY_DOCENTE_ID, si no, desde KEY_USER_ID
     */
    fun getDocenteId(): Int {
        // 1. Intentar desde KEY_DOCENTE_ID (si se guardó específicamente)
        val docenteId = prefs.getInt(KEY_DOCENTE_ID, -1)
        if (docenteId != -1) {
            return docenteId
        }

        // 2. Intentar desde KEY_USER_ID (si es docente)
        val rol = getRol()
        val userId = getUserId()
        return if (rol == UserRole.DOCENTE && userId != null && userId > 0) {
            userId
        } else {
            -1
        }
    }

    /**
     * Verifica si el usuario es docente
     */
    fun isDocente(): Boolean {
        return getRol() == UserRole.DOCENTE
    }
}