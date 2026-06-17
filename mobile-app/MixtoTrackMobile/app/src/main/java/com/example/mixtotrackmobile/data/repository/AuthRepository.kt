package com.example.mixtotrackmobile.data.repository

import com.example.mixtotrackmobile.data.models.request.LoginRequest
import com.example.mixtotrackmobile.data.models.response.LoginResponse
import com.example.mixtotrackmobile.data.network.ApiService
import com.example.mixtotrackmobile.utils.SessionManager
import com.example.mixtotrackmobile.utils.UserRole
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {

    suspend fun login(
        email: String,
        password: String,
        rol: UserRole
    ): Result<LoginResponse> {
        return try {
            // Simular delay de red (opcional)
            delay(500)

            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    // Guardar token y sesión
                    sessionManager.guardarSesion(
                        email = email,
                        rol = rol,
                        token = loginResponse.access,
                        nombre = loginResponse.nombre,
                        grado = loginResponse.grado
                    )
                    Result.success(loginResponse)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Manejar errores HTTP
                val errorMessage = when (response.code()) {
                    401 -> "Credenciales incorrectas"
                    400 -> "Datos inválidos"
                    404 -> "Servidor no encontrado"
                    500 -> "Error interno del servidor"
                    else -> "Error: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            // Error de red o conexión
            Result.failure(
                Exception("Error de conexión: ${e.message ?: "Intenta nuevamente"}")
            )
        }
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun getUserRole(): UserRole? {
        return sessionManager.getRol()
    }

    fun logout() {
        sessionManager.cerrarSesion()
    }
}