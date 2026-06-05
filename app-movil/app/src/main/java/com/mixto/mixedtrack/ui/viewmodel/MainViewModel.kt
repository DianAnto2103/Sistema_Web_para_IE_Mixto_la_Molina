package com.mixto.mixedtrack.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mixto.mixedtrack.data.model.*
import com.mixto.mixedtrack.data.network.RetrofitClient
import com.mixto.mixedtrack.data.repository.MainRepository
import com.mixto.mixedtrack.util.Resource
import com.mixto.mixedtrack.util.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefManager = SharedPrefManager(application.applicationContext)
    private val repository = MainRepository(RetrofitClient.apiService)

    private val _loginState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    private val _gradesState = MutableStateFlow<Resource<List<Grade>>>(Resource.Idle)
    private val _workshopsState = MutableStateFlow<Resource<List<Workshop>>>(Resource.Idle)
    private val _requestState = MutableStateFlow<Resource<String>>(Resource.Idle)

    val loginState: StateFlow<Resource<Unit>> = _loginState.asStateFlow()
    val gradesState: StateFlow<Resource<List<Grade>>> = _gradesState.asStateFlow()
    val workshopsState: StateFlow<Resource<List<Workshop>>> = _workshopsState.asStateFlow()
    val requestState: StateFlow<Resource<String>> = _requestState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Por favor completa usuario y contraseña")
            return
        }

        _loginState.value = Resource.Loading

        // Bypass para pruebas locales (admin/admin)
        if (username.trim() == "admin" && password == "admin") {
            viewModelScope.launch {
                kotlinx.coroutines.delay(1000) // Simular latencia
                sharedPrefManager.saveToken("mock_token_admin")
                _loginState.value = Resource.Success(Unit)
            }
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.login(LoginRequest(username.trim(), password))
                sharedPrefManager.saveToken(response.access)
                _loginState.value = Resource.Success(Unit)
            } catch (exception: Exception) {
                _loginState.value = Resource.Error(parseError(exception))
            }
        }
    }

    fun loadGrades() {
        val token = sharedPrefManager.getToken()
        if (token.isNullOrEmpty()) {
            _gradesState.value = Resource.Error("Sesión inválida. Inicia sesión de nuevo.")
            return
        }

        _gradesState.value = Resource.Loading
        viewModelScope.launch {
            try {
                val grades = repository.getGrades(token)
                if (grades.isEmpty()) {
                    _gradesState.value = Resource.Error("No se encontraron calificaciones")
                } else {
                    _gradesState.value = Resource.Success(grades)
                }
            } catch (exception: Exception) {
                _gradesState.value = Resource.Error(parseError(exception))
            }
        }
    }

    fun loadWorkshops() {
        val token = sharedPrefManager.getToken()
        if (token.isNullOrEmpty()) {
            _workshopsState.value = Resource.Error("Sesión inválida. Inicia sesión de nuevo.")
            return
        }

        _workshopsState.value = Resource.Loading
        viewModelScope.launch {
            try {
                val workshops = repository.getWorkshops(token)
                if (workshops.isEmpty()) {
                    _workshopsState.value = Resource.Error("No hay talleres disponibles")
                } else {
                    _workshopsState.value = Resource.Success(workshops)
                }
            } catch (exception: Exception) {
                _workshopsState.value = Resource.Error(parseError(exception))
            }
        }
    }

    fun requestWorkshop(workshopId: Int) {
        val token = sharedPrefManager.getToken()
        if (token.isNullOrEmpty()) {
            _requestState.value = Resource.Error("Sesión inválida. Inicia sesión de nuevo.")
            return
        }

        _requestState.value = Resource.Loading
        viewModelScope.launch {
            try {
                val response = repository.requestWorkshop(token, WorkshopRequest(workshopId))
                _requestState.value = Resource.Success(response.message)
            } catch (exception: Exception) {
                _requestState.value = Resource.Error(parseError(exception))
            }
        }
    }

    fun getSavedToken(): String? {
        return sharedPrefManager.getToken()
    }

    fun logout() {
        sharedPrefManager.clearSession()
    }

    private fun parseError(exception: Exception): String {
        return when {
            exception.message.isNullOrBlank() -> "Error de conexión. Revisa tu red."
            exception.message?.contains("Unable to resolve host") == true -> "No se pudo conectar al servidor. Revisa tu conexión."
            exception is HttpException -> {
                when (exception.code()) {
                    400 -> "Usuario o contraseña incorrectos"
                    401 -> "Credenciales inválidas"
                    404 -> "Recurso no encontrado"
                    else -> "Error del servidor: ${exception.code()}"
                }
            }
            else -> exception.message ?: "Ocurrió un error inesperado"
        }
    }
}
