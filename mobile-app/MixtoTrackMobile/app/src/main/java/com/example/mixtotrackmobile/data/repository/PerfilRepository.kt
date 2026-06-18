package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.response.PerfilResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerfilRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getPerfil(): Result<PerfilResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getPerfil("Bearer $token")

            if (response.isSuccessful) {
                val perfil = response.body()
                if (perfil != null) {
                    // Actualizar datos en SessionManager
                    sessionManager.guardarDatosPerfil(
                        nombre = perfil.nombre,
                        grado = perfil.grado,
                        departamento = perfil.departamento
                    )
                    Result.success(perfil)
                } else {
                    Result.failure(Exception("Datos de perfil vacíos"))
                }
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Sesión expirada"
                    404 -> "Perfil no encontrado"
                    else -> "Error al cargar perfil: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}