package com.mixto.mixedtrack.data.network

import com.mixto.mixedtrack.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/calificaciones/")
    suspend fun getGrades(@Header("Authorization") authHeader: String): List<Grade>

    @GET("api/talleres/")
    suspend fun getWorkshops(@Header("Authorization") authHeader: String): List<Workshop>

    @POST("api/talleres/solicitar/")
    suspend fun requestWorkshop(
        @Header("Authorization") authHeader: String,
        @Body request: WorkshopRequest
    ): GenericResponse
}
