package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.request.CalificacionRequest
import com.example.mixtotrackmobile.data.models.response.CalificacionResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalificacionRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun registrarCalificacion(
        alumnoId: Int,
        cursoId: Int,
        bimestre: Int,
        nota: Double
    ): Result<CalificacionResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            if (nota < 0 || nota > 20) {
                return Result.failure(Exception("La nota debe estar entre 0 y 20"))
            }

            val request = CalificacionRequest(
                alumnoId = alumnoId,
                cursoId = cursoId,
                bimestre = bimestre,
                nota = nota
            )

            val response = apiService.registrarCalificacion("Bearer $token", request)

            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Respuesta vacía"))
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Datos inválidos. Verifica los campos."
                    404 -> "Alumno o curso no encontrado"
                    409 -> "Ya existe una calificación para este bimestre"
                    else -> "Error al registrar calificación: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}