package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.request.CrearGrupoRequest
import com.example.mixtotrackmobile.data.models.response.GrupoEstudioResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GrupoEstudioRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun getGrupos(): Result<List<GrupoEstudioResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getGruposEstudio("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar grupos de estudio"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun crearGrupo(
        nombre: String,
        curso: String,
        descripcion: String,
        cupos: Int
    ): Result<GrupoEstudioResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val request = CrearGrupoRequest(
                nombre = nombre,
                curso = curso,
                descripcion = descripcion,
                cupos = cupos
            )

            val response = apiService.crearGrupo("Bearer $token", request)

            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error al crear grupo"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun unirseAGrupo(grupoId: Int): Result<Unit> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.unirseAGrupo("Bearer $token", grupoId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "No hay cupos disponibles"
                    409 -> "Ya eres miembro de este grupo"
                    else -> "Error al unirse al grupo"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun salirDelGrupo(grupoId: Int): Result<Unit> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.salirDelGrupo("Bearer $token", grupoId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al salir del grupo"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun eliminarGrupo(grupoId: Int): Result<Unit> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.eliminarGrupo("Bearer $token", grupoId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar grupo"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}