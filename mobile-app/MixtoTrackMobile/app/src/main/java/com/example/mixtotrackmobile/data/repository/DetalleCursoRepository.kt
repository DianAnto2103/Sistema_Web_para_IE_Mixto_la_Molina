package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.response.DetalleCursoResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetalleCursoRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getDetalleCurso(cursoId: Int): Result<DetalleCursoResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getDetalleCurso("Bearer $token", cursoId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Datos del curso vacíos"))
                }
            } else {
                val errorMsg = when (response.code()) {
                    404 -> "Curso no encontrado"
                    401 -> "Sesión expirada"
                    else -> "Error al cargar detalle del curso"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}