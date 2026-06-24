package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.entities.Alumno
import com.example.mixtotrackmobile.data.models.entities.Calificacion
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.data.models.entities.Horario
import com.example.mixtotrackmobile.data.models.request.CalificacionRequest
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

    private suspend fun getToken(): String = "Bearer ${sessionManager.getToken() ?: ""}"

    // ========== CURSOS ==========
    suspend fun listarCursosDocente(docenteId: Int): List<Curso> {
        return try {
            val response = apiService.getCursos(getToken())
            if (response.isSuccessful) {
                // Curso tiene "docente: Int"
                response.body()?.filter { it.docente == docenteId } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ========== ALUMNOS ==========
    suspend fun listarAlumnos(): List<Alumno> {
        return try {
            val response = apiService.getAlumnos(getToken())
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun buscarAlumnos(query: String): List<Alumno> {
        return try {
            val alumnos = listarAlumnos()
            alumnos.filter { alumno ->
                alumno.nombres.contains(query, ignoreCase = true) ||
                        alumno.apellidos.contains(query, ignoreCase = true)
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ========== CALIFICACIONES ==========
    suspend fun obtenerCalificacionesAlumno(alumnoId: Int): List<Calificacion> {
        return try {
            val response = apiService.getCalificaciones(getToken(), alumnoId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun crearCalificacion(
        alumnoId: Int,
        cursoId: Int,
        bimestre: Int,
        nota: Double,
        observacion: String?
    ): Calificacion? {
        return try {
            val request = CalificacionRequest(
                alumno = alumnoId,
                curso = cursoId,
                nota = nota,
                bimestre = bimestre
            )
            val response = apiService.createCalificacion(getToken(), request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun actualizarCalificacion(
        id: Int,
        alumnoId: Int,
        cursoId: Int,
        bimestre: Int,
        nota: Double,
        observacion: String?
    ): Calificacion? {
        return try {
            val request = CalificacionRequest(
                alumno = alumnoId,
                curso = cursoId,
                nota = nota,
                bimestre = bimestre
            )
            val response = apiService.updateCalificacion(getToken(), id, request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun eliminarCalificacion(id: Int): Boolean {
        return try {
            val response = apiService.deleteCalificacion(getToken(), id)
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }

    // ========== HORARIOS ==========
    suspend fun listarHorariosDocente(docenteId: Int): List<Horario> {
        return try {
            val response = apiService.getHorarios(getToken(), docenteId)
            if (response.isSuccessful) {
                response.body()?.map { horarioResponse ->
                    Horario(
                        id = horarioResponse.id,
                        docente = horarioResponse.docenteId,  // ← docenteId (Int)
                        dia = horarioResponse.dia,            // ← dia (String)
                        hora_inicio = horarioResponse.horaInicio,
                        hora_fin = horarioResponse.horaFin,
                        curso = null
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ========== PERFIL ==========
    suspend fun obtenerPerfilDocente(): PerfilResponse? {
        return try {
            val response = apiService.getPerfil(getToken())
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