package com.example.mixtotrackmobile.ui.docente.horarios

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.HorarioResponse
import com.example.mixtotrackmobile.data.repository.HorarioRepository
import com.example.mixtotrackmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorariosViewModel @Inject constructor(
    private val horarioRepository: HorarioRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _horarios = MutableLiveData<List<HorarioResponse>>()
    val horarios: LiveData<List<HorarioResponse>> = _horarios

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    fun cargarHorarios() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val docenteId = sessionManager.getDocenteId()
            val result = horarioRepository.getHorarios(
                if (docenteId != -1) docenteId else null
            )

            result.fold(
                onSuccess = { lista ->
                    _horarios.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun actualizarDisponibilidad(horarioId: Int, disponible: Boolean) {
        viewModelScope.launch {
            _error.value = null
            _mensaje.value = null

            val result = horarioRepository.actualizarDisponibilidad(horarioId, disponible)

            result.fold(
                onSuccess = {
                    _mensaje.value = if (disponible) {
                        "Horario disponible"
                    } else {
                        "Horario no disponible"
                    }
                    // Actualizar lista localmente
                    _horarios.value = _horarios.value?.map { horario ->
                        if (horario.id == horarioId) {
                            horario.copy(disponible = disponible)
                        } else {
                            horario
                        }
                    }
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )
        }
    }
}