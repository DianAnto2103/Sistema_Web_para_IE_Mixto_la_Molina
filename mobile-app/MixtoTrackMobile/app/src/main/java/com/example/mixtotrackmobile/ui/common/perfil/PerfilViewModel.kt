package com.example.mixtotrackmobile.ui.common.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.entities.PerfilData
import com.example.mixtotrackmobile.data.repository.PerfilRepository
import com.example.mixtotrackmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val perfilRepository: PerfilRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _perfilData = MutableLiveData<PerfilData?>()
    val perfilData: LiveData<PerfilData?> = _perfilData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarPerfil() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = perfilRepository.getPerfil()

            result.fold(
                onSuccess = { response ->
                    _perfilData.value = PerfilData(
                        id = response.id,
                        nombre = response.nombre,
                        apellido = response.apellido,
                        email = response.email,
                        rol = response.rol,
                        grado = response.grado,
                        seccion = response.seccion,
                        promedioGeneral = response.promedioGeneral,
                        departamento = response.departamento,
                        especialidad = response.especialidad,
                        añosExperiencia = response.añosExperiencia
                    )
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun getRol(): String? {
        return sessionManager.getRol()?.name
    }
}
