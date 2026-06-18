package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.response.HorarioResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HorarioRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getHorarios(docenteId: Int? = null): Result<List<HorarioResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = if (docenteId != null) {
                apiService.getHorarios("Bearer $token", docenteId)
            } else {
                apiService.getHorarios("Bearer $token", null)
            }

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar horarios"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}