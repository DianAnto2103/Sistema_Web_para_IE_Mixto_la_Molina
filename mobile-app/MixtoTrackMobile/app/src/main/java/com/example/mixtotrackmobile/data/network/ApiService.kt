package com.example.mixtotrackmobile.data.network

import com.example.mixtotrackmobile.data.models.entities.Alumno
import com.example.mixtotrackmobile.data.models.entities.Bimestre
import com.example.mixtotrackmobile.data.models.entities.Calificacion
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.data.models.entities.Docente
import com.example.mixtotrackmobile.data.models.entities.Horario
import com.example.mixtotrackmobile.data.models.entities.Taller
import com.example.mixtotrackmobile.data.models.request.CalificacionRequest
import com.example.mixtotrackmobile.data.models.request.LoginRequest
import com.example.mixtotrackmobile.data.models.request.SolicitudTallerRequest
import com.example.mixtotrackmobile.data.models.response.LoginResponse
import com.example.mixtotrackmobile.data.models.response.TallerResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== AUTENTICACIÓN ==========
    @POST("api/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // ========== ALUMNOS ==========
    @GET("api/alumnos/")
    suspend fun getAlumnos(
        @Header("Authorization") token: String
    ): Response<List<Alumno>>

    @GET("api/alumnos/{id}/")
    suspend fun getAlumno(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Alumno>

    // ========== CURSOS ==========
    @GET("api/cursos/")
    suspend fun getCursos(
        @Header("Authorization") token: String
    ): Response<List<Curso>>

    @GET("api/cursos/{id}/")
    suspend fun getCurso(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Curso>

    // ========== CALIFICACIONES ==========
    @GET("api/calificaciones/")
    suspend fun getCalificaciones(
        @Header("Authorization") token: String,
        @Query("alumno") alumnoId: Int? = null
    ): Response<List<Calificacion>>

    @POST("api/calificaciones/")
    suspend fun createCalificacion(
        @Header("Authorization") token: String,
        @Body request: CalificacionRequest
    ): Response<Calificacion>

    @PUT("api/calificaciones/{id}/")
    suspend fun updateCalificacion(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: CalificacionRequest
    ): Response<Calificacion>

    @DELETE("api/calificaciones/{id}/")
    suspend fun deleteCalificacion(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // ========== TALLERES ==========
    @GET("api/talleres/")
    suspend fun getTalleres(
        @Header("Authorization") token: String
    ): Response<List<Taller>>

    @POST("api/solicitar-taller/")
    suspend fun solicitarTaller(
        @Header("Authorization") token: String,
        @Body request: SolicitudTallerRequest
    ): Response<TallerResponse>

    // ========== HORARIOS ==========
    @GET("api/horarios/")
    suspend fun getHorarios(
        @Header("Authorization") token: String,
        @Query("docente") docenteId: Int? = null
    ): Response<List<Horario>>

    // ========== BIMESTRES ==========
    @GET("api/bimestres/")
    suspend fun getBimestres(
        @Header("Authorization") token: String
    ): Response<List<Bimestre>>

    // ========== DOCENTES ==========
    @GET("api/docentes/")
    suspend fun getDocentes(
        @Header("Authorization") token: String
    ): Response<List<Docente>>

    @GET("api/docentes/{id}/")
    suspend fun getDocente(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Docente>
}