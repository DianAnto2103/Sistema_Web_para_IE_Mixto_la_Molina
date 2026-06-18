package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.request.SolicitudTallerRequest
import com.example.mixtotrackmobile.data.models.response.SolicitudTallerResponse
import com.example.mixtotrackmobile.data.models.response.TallerResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TallerRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getTalleres(): Result<List<TallerResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getTalleres("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar talleres"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun solicitarTaller(tallerId: Int): Result<SolicitudTallerResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            // Obtener ID del alumno desde SessionManager
            val alumnoId = sessionManager.getUserId() ?: return Result.failure(Exception("Usuario no encontrado"))

            val request = SolicitudTallerRequest(
                tallerId = tallerId,
                alumnoId = alumnoId
            )

            val response = apiService.solicitarTaller("Bearer $token", request)

            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Respuesta vacía"))
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "No hay cupos disponibles"
                    409 -> "Ya solicitaste este taller"
                    else -> "Error al solicitar taller"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun getMisTalleres(): Result<List<SolicitudTallerResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getMisTalleres("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar tus talleres"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}