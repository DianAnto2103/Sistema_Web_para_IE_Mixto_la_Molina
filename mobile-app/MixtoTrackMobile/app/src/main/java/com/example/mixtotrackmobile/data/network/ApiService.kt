package com.example.mixtotrackmobile.data.network

import com.example.mixtotrackmobile.data.models.entities.Alumno
import com.example.mixtotrackmobile.data.models.entities.Bimestre
import com.example.mixtotrackmobile.data.models.entities.Calificacion
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.data.models.entities.Docente
import com.example.mixtotrackmobile.data.models.entities.Horario
import com.example.mixtotrackmobile.data.models.entities.Taller
import com.example.mixtotrackmobile.data.models.request.CalificacionRequest
import com.example.mixtotrackmobile.data.models.request.CrearGrupoRequest
import com.example.mixtotrackmobile.data.models.request.LoginRequest
import com.example.mixtotrackmobile.data.models.request.SolicitudTallerRequest
import com.example.mixtotrackmobile.data.models.response.CursoResponse
import com.example.mixtotrackmobile.data.models.response.GrupoEstudioResponse
import com.example.mixtotrackmobile.data.models.response.LoginResponse
import com.example.mixtotrackmobile.data.models.response.NotificacionResponse
import com.example.mixtotrackmobile.data.models.response.PerfilResponse
import com.example.mixtotrackmobile.data.models.response.SolicitudTallerResponse
import com.example.mixtotrackmobile.data.models.response.TallerResponse
import com.example.mixtotrackmobile.data.models.response.HorarioResponse
import com.example.mixtotrackmobile.data.models.response.RendimientoResponse

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

    // ========== PERFIL ==========
    @GET("api/perfil/")
    suspend fun getPerfil(
        @Header("Authorization") token: String
    ): Response<PerfilResponse>

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

    @GET("api/mis-cursos/")
    suspend fun getMisCursos(
        @Header("Authorization") token: String
    ): Response<List<CursoResponse>>

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
    ): Response<List<TallerResponse>>

    @GET("api/talleres/{id}/")
    suspend fun getTaller(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<TallerResponse>

    @POST("api/solicitar-taller/")
    suspend fun solicitarTaller(
        @Header("Authorization") token: String,
        @Body request: SolicitudTallerRequest
    ): Response<SolicitudTallerResponse>

    @GET("api/mis-talleres/")
    suspend fun getMisTalleres(
        @Header("Authorization") token: String
    ): Response<List<SolicitudTallerResponse>>

    // ========== HORARIOS ==========
    @GET("api/horarios/")
    suspend fun getHorarios(
        @Header("Authorization") token: String,
        @Query("docente") docenteId: Int? = null
    ): Response<List<HorarioResponse>>

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

    // ========== NOTIFICACIONES ==========
    @GET("api/notificaciones/")
    suspend fun getNotificaciones(
        @Header("Authorization") token: String
    ): Response<List<NotificacionResponse>>

    @POST("api/notificaciones/{id}/leer/")
    suspend fun marcarComoLeida(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @POST("api/notificaciones/marcar-todas-leidas/")
    suspend fun marcarTodasComoLeidas(
        @Header("Authorization") token: String
    ): Response<Unit>

    // ========== GRUPOS DE ESTUDIO ==========
    @GET("api/grupos-estudio/")
    suspend fun getGruposEstudio(
        @Header("Authorization") token: String
    ): Response<List<GrupoEstudioResponse>>

    @POST("api/grupos-estudio/")
    suspend fun crearGrupo(
        @Header("Authorization") token: String,
        @Body request: CrearGrupoRequest
    ): Response<GrupoEstudioResponse>

    @POST("api/grupos-estudio/{id}/unirse/")
    suspend fun unirseAGrupo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @POST("api/grupos-estudio/{id}/salir/")
    suspend fun salirDelGrupo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE("api/grupos-estudio/{id}/")
    suspend fun eliminarGrupo(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // ========== RENDIMIENTO ACADÉMICO ==========
    @GET("api/rendimiento/")
    suspend fun getRendimiento(
        @Header("Authorization") token: String,
        @Query("curso_id") cursoId: Int
    ): Response<RendimientoResponse>



}