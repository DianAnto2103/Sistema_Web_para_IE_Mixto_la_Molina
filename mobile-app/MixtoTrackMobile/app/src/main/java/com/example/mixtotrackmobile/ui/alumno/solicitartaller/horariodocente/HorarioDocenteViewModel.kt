package com.example.mixtotrackmobile.ui.alumno.solicitartaller.horariodocente

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.HorarioResponse
import com.example.mixtotrackmobile.data.repository.HorarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HorarioDocenteViewModel @Inject constructor(
    private val horarioRepository: HorarioRepository
) : ViewModel() {

    private val _allHorarios = MutableLiveData<List<HorarioResponse>>()
    private val _horariosFiltrados = MutableLiveData<List<HorarioResponse>>()
    val horariosFiltrados: LiveData<List<HorarioResponse>> = _horariosFiltrados

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarHorarios(docenteId: Int? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = horarioRepository.getHorarios(docenteId)

            result.fold(
                onSuccess = { lista ->
                    _allHorarios.value = lista
                    _horariosFiltrados.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun filtrarHorarios(query: String) {
        val lista = _allHorarios.value ?: return

        if (query.isEmpty()) {
            _horariosFiltrados.value = lista
            return
        }

        val filtrados = lista.filter { horario ->
            horario.docente.contains(query, ignoreCase = true)
        }
        _horariosFiltrados.value = filtrados
    }
}