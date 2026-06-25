package com.example.mixtotrackmobile.data.network

import com.example.mixtotrackmobile.data.models.entities.Alumno
import com.example.mixtotrackmobile.data.models.entities.Bimestre
import com.example.mixtotrackmobile.data.models.entities.Calificacion
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.data.models.entities.Docente
import com.example.mixtotrackmobile.data.models.request.CalificacionRequest
import com.example.mixtotrackmobile.data.models.request.CrearGrupoRequest
import com.example.mixtotrackmobile.data.models.request.CrearTallerRequest
import com.example.mixtotrackmobile.data.models.request.LoginRequest
import com.example.mixtotrackmobile.data.models.request.SolicitudTallerRequest
import com.example.mixtotrackmobile.data.models.response.AlumnoResponse
import com.example.mixtotrackmobile.data.models.response.CalificacionResponse
import com.example.mixtotrackmobile.data.models.response.CursoDocenteResponse
import com.example.mixtotrackmobile.data.models.response.CursoResponse
import com.example.mixtotrackmobile.data.models.response.DetalleCursoDocenteResponse
import com.example.mixtotrackmobile.data.models.response.DetalleCursoResponse
import com.example.mixtotrackmobile.data.models.response.GrupoEstudioResponse
import com.example.mixtotrackmobile.data.models.response.LoginResponse
import com.example.mixtotrackmobile.data.models.response.NotificacionResponse
import com.example.mixtotrackmobile.data.models.response.PerfilResponse
import com.example.mixtotrackmobile.data.models.response.TallerResponse
import com.example.mixtotrackmobile.data.models.response.HorarioResponse
import com.example.mixtotrackmobile.data.models.response.RendimientoResponse
import com.example.mixtotrackmobile.data.models.response.SolicitudTallerResponse
import com.example.mixtotrackmobile.data.models.response.TallerDocenteResponse

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
    @POST("api/calificaciones/")
    suspend fun registrarCalificacion(
        @Header("Authorization") token: String,
        @Body request: CalificacionRequest
    ): Response<CalificacionResponse>


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

    // ========== CALIFICACIONES (adicional) ==========
    @GET("api/calificaciones/")
    suspend fun getCalificaciones(
        @Header("Authorization") token: String,
        @Query("alumno") alumnoId: Int
    ): Response<List<CalificacionResponse>>

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


    // ========== TALLERES (DOCENTE) ==========
    @GET("api/docente/talleres/")
    suspend fun getTalleresDocente(
        @Header("Authorization") token: String
    ): Response<List<TallerDocenteResponse>>


    @POST("api/docente/talleres/")
    suspend fun crearTaller(
        @Header("Authorization") token: String,
        @Body request: CrearTallerRequest
    ): Response<TallerDocenteResponse>

    @PUT("api/docente/talleres/{id}/estado/")
    suspend fun cambiarEstadoTaller(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, String>
    ): Response<Unit>

    @DELETE("api/docente/talleres/{id}/")
    suspend fun eliminarTaller(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // ========== HORARIOS ==========
    @GET("api/horarios/")
    suspend fun getHorarios(
        @Header("Authorization") token: String,
        @Query("docente") docenteId: Int? = null
    ): Response<List<HorarioResponse>>

    @PUT("api/horarios/{id}/disponibilidad/")
    suspend fun actualizarDisponibilidad(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: Map<String, Boolean>
    ): Response<Unit>

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

    @GET("api/docente/cursos/")
    suspend fun getCursosDocente(
        @Header("Authorization") token: String
    ): Response<List<CursoDocenteResponse>>

    // ========== DETALLE CURSO DOCENTE ==========
    @GET("api/docente/curso/{id}/detalle/")
    suspend fun getDetalleCursoDocente(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DetalleCursoDocenteResponse>

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

    // ========== DETALLE CURSO ==========
    @GET("api/curso/{id}/detalle/")
    suspend fun getDetalleCurso(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DetalleCursoResponse>


    // ========== BUSCAR ALUMNOS ==========
    @GET("api/buscar-alumnos/")
    suspend fun buscarAlumnos(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ): Response<List<AlumnoResponse>>

}
