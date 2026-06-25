package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.entities.Alumno
import com.example.mixtotrackmobile.data.models.entities.Calificacion
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.data.models.entities.Horario
import com.example.mixtotrackmobile.data.models.request.CalificacionRequest
import com.example.mixtotrackmobile.data.models.response.AlumnoResponse
import com.example.mixtotrackmobile.data.models.response.CalificacionResponse
import com.example.mixtotrackmobile.data.models.response.CursoDocenteResponse
import com.example.mixtotrackmobile.data.models.response.PerfilResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocenteRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    private suspend fun getToken(): String? {
        return sessionManager.getToken()
    }

    // ========== CURSOS ==========
    suspend fun listarCursosDocente(docenteId: Int): List<Curso> {
        return try {
            val token = getToken()
            if (token == null) return emptyList()

            val response = apiService.getCursos("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.filter { it.docente == docenteId } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ========== OBTENER CURSOS DEL DOCENTE (CON RESULT) ==========
    suspend fun getCursosDocente(): Result<List<CursoDocenteResponse>> {
        return try {
            val token = sessionManager.getToken()
            if (token == null) {
                return Result.failure(Exception("No hay sesión activa"))
            }

            val response = apiService.getCursosDocente("Bearer $token")

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Sesión expirada"
                    404 -> "No se encontraron cursos"
                    else -> "Error al cargar cursos: ${response.code()}"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    // ========== BUSCAR ALUMNOS (USANDO API) ==========
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

    // ========== CALIFICACIONES ==========
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

    // ========== PERFIL ==========
    suspend fun obtenerPerfilDocente(): PerfilResponse? {
        return try {
            val token = getToken()
            if (token == null) return null

            val response = apiService.getPerfil("Bearer $token")
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}