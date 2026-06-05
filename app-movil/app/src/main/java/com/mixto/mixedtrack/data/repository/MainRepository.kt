package com.mixto.mixedtrack.data.repository

import com.mixto.mixedtrack.data.model.*
import com.mixto.mixedtrack.data.network.ApiService

class MainRepository(private val apiService: ApiService) {

    suspend fun login(request: LoginRequest): LoginResponse {
        return apiService.login(request)
    }

    suspend fun getGrades(token: String): List<Grade> {
        return apiService.getGrades("Bearer $token")
    }

    suspend fun getWorkshops(token: String): List<Workshop> {
        return apiService.getWorkshops("Bearer $token")
    }

    suspend fun requestWorkshop(token: String, request: WorkshopRequest): GenericResponse {
        return apiService.requestWorkshop("Bearer $token", request)
    }
}
