package com.example.mixtotrackmobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.repository.AuthRepository
import com.example.mixtotrackmobile.utils.SessionManager
import com.example.mixtotrackmobile.utils.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, rol: UserRole) {
        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = authRepository.login(email, password, rol)

            result.fold(
                onSuccess = { loginResponse ->
                    _uiState.value = LoginUiState.Success(rol)
                },
                onFailure = { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }

    fun guardarRol(rol: UserRole) {
        sessionManager.guardarRol(rol)
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun getCurrentUserRole(): UserRole? {
        return sessionManager.getRol()
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val rol: UserRole) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}