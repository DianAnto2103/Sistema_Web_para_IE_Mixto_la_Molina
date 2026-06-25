package com.example.mixtotrackmobile.ui.docente.cursos.busqueda_alumno

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.AlumnoResponse
import com.example.mixtotrackmobile.data.repository.AlumnoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusquedaAlumnoViewModel @Inject constructor(
    private val alumnoRepository: AlumnoRepository
) : ViewModel() {

    private val _alumnos = MutableLiveData<List<AlumnoResponse>>(emptyList())
    val alumnos: LiveData<List<AlumnoResponse>> = _alumnos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun buscarAlumnos(query: String) {
        if (query.length < 2) {
            _alumnos.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = alumnoRepository.buscarAlumnos(query)

            result.fold(
                onSuccess = { lista ->
                    _alumnos.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}