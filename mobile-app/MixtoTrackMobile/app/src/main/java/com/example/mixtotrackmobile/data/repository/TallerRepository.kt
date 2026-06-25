package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.request.CrearTallerRequest
import com.example.mixtotrackmobile.data.models.request.SolicitudTallerRequest
import com.example.mixtotrackmobile.data.models.response.SolicitudTallerResponse
import com.example.mixtotrackmobile.data.models.response.TallerDocenteResponse
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


    // ========== TALLERES DOCENTE ==========
    suspend fun getTalleresDocente(): Result<List<TallerDocenteResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getTalleresDocente("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar talleres"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun cambiarEstadoTaller(tallerId: Int, estado: String): Result<Unit> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.cambiarEstadoTaller(
                "Bearer $token",
                tallerId,
                mapOf("estado" to estado)
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al cambiar estado"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    suspend fun eliminarTaller(tallerId: Int): Result<Unit> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.eliminarTaller("Bearer $token", tallerId)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar taller"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    // ========== MIS SOLICITUDES (ALUMNO) ==========
    suspend fun getMisSolicitudes(): Result<List<SolicitudTallerResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getMisTalleres("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al cargar tus solicitudes"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    // ========== CREAR TALLER ==========
    suspend fun crearTaller(
        nombre: String,
        descripcion: String,
        curso: String,
        horario: String,
        cupos: Int,
        fechaInicio: String,
        fechaFin: String
    ): Result<TallerDocenteResponse> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            if (nombre.isEmpty()) {
                return Result.failure(Exception("El nombre es obligatorio"))
            }
            if (descripcion.isEmpty()) {
                return Result.failure(Exception("La descripción es obligatoria"))
            }
            if (curso.isEmpty()) {
                return Result.failure(Exception("El curso es obligatorio"))
            }
            if (horario.isEmpty()) {
                return Result.failure(Exception("El horario es obligatorio"))
            }
            if (cupos <= 0) {
                return Result.failure(Exception("Los cupos deben ser mayor a 0"))
            }

            val request = CrearTallerRequest(
                nombre = nombre,
                descripcion = descripcion,
                curso = curso,
                horario = horario,
                cupos = cupos,
                fechaInicio = fechaInicio,
                fechaFin = fechaFin
            )

            val response = apiService.crearTaller("Bearer $token", request)

            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("Respuesta vacía"))
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Datos inválidos. Verifica los campos."
                    409 -> "Ya existe un taller con ese nombre"
                    else -> "Error al crear taller: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}