package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.response.CursoResponse
import com.example.mixtotrackmobile.data.models.response.DetalleCursoDocenteResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CursoRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getMisCursos(): Result<List<CursoResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getMisCursos("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar tus cursos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    // ========== DETALLE CURSO DOCENTE ==========
    suspend fun getDetalleCursoDocente(cursoId: Int): Result<DetalleCursoDocenteResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getDetalleCursoDocente("Bearer $token", cursoId)

            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Datos vacíos"))
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Sesión expirada"
                    404 -> "Curso no encontrado"
                    else -> "Error al cargar detalle del curso"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}