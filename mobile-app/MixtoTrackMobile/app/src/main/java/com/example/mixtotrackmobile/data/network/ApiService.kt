package com.example.mixtotrackmobile.data.network

import com.example.mixtotrackmobile.data.model.*
import retrofit2.http.*

interface ApiService {

    //AUTENTICACIÓN
    @POST("api/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    //ALUMNOS
    @GET("api/alumnos/")
    suspend fun getAlumnos(@Header("Authorization") token: String): List<Alumno>

    //CURSOS 
    @GET("api/cursos/")
    suspend fun getCursos(@Header("Authorization") token: String): List<Curso>

    //CALIFICACIONES 
    @GET("api/calificaciones/")
    suspend fun getCalificaciones(@Header("Authorization") token: String): List<Calificacion>

    @POST("api/calificaciones/")
    suspend fun createCalificacion(
        @Header("Authorization") token: String,
        @Body request: CalificacionRequest
    ): Calificacion

    //TALLERES
    @GET("api/talleres/")
    suspend fun getTalleres(@Header("Authorization") token: String): List<Taller>

    @POST("api/solicitar-taller/")
    suspend fun solicitarTaller(
        @Header("Authorization") token: String,
        @Body request: SolicitudTallerRequest
    ): SolicitudTallerResponse

    //HORARIOS
    @GET("api/horarios/")
    suspend fun getHorarios(@Header("Authorization") token: String): List<Horario>

    //BIMESTRES
    @GET("api/bimestres/")
    suspend fun getBimestres(@Header("Authorization") token: String): List<Bimestre>

    //DOCENTES
    @GET("api/docentes/")
    suspend fun getDocentes(@Header("Authorization") token: String): List<Docente>
}