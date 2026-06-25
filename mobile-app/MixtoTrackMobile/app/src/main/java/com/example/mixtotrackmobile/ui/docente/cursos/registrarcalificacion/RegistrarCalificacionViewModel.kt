package com.example.mixtotrackmobile.ui.docente.cursos.registrar_calificacion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.repository.CalificacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrarCalificacionViewModel @Inject constructor(
    private val calificacionRepository: CalificacionRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _exito = MutableLiveData(false)
    val exito: LiveData<Boolean> = _exito

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun registrarCalificacion(alumnoId: Int, cursoId: Int, bimestre: Int, nota: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _exito.value = false

            val result = calificacionRepository.registrarCalificacion(
                alumnoId = alumnoId,
                cursoId = cursoId,
                bimestre = bimestre,
                nota = nota
            )

            result.fold(
                onSuccess = { calificacion ->
                    _exito.value = true
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun resetearEstado() {
        _exito.value = false
        _error.value = null
    }
}