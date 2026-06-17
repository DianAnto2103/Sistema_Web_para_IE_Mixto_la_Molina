package com.example.mixtotrackmobile.ui.alumno.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.repository.AuthRepository
import com.example.mixtotrackmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuAlumnoViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _nombreAlumno = MutableLiveData("Alumno")
    val nombreAlumno: LiveData<String> = _nombreAlumno

    private val _gradoAlumno = MutableLiveData("Cargando...")
    val gradoAlumno: LiveData<String> = _gradoAlumno

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val nombre = sessionManager.getNombre() ?: "Alumno"
        val grado = sessionManager.getGrado() ?: "Sin grado asignado"

        _nombreAlumno.value = nombre
        _gradoAlumno.value = grado
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            _isLoading.value = true
            authRepository.logout()
            _isLoading.value = false
        }
    }

    fun getNombreUsuario(): String {
        return sessionManager.getEmail()?.split("@")?.first() ?: "Usuario"
    }
}