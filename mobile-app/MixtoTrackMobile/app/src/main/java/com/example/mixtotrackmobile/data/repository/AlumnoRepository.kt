package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.response.AlumnoResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlumnoRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun buscarAlumnos(query: String): Result<List<AlumnoResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            if (query.length < 2) {
                return Result.success(emptyList())
            }

            val response = apiService.buscarAlumnos("Bearer $token", query)

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Sesión expirada"
                    404 -> "No se encontraron alumnos"
                    else -> "Error al buscar alumnos: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}