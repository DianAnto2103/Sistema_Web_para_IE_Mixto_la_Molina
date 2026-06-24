package com.example.mixtotrackmobile.ui.alumno.detalle_curso

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.DetalleCursoResponse
import com.example.mixtotrackmobile.data.repository.DetalleCursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleCursoViewModel @Inject constructor(
    private val detalleCursoRepository: DetalleCursoRepository
) : ViewModel() {

    private val _detalleCurso = MutableLiveData<DetalleCursoResponse?>()
    val detalleCurso: LiveData<DetalleCursoResponse?> = _detalleCurso

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarDetalleCurso(cursoId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = detalleCursoRepository.getDetalleCurso(cursoId)

            result.fold(
                onSuccess = { detalle ->
                    _detalleCurso.value = detalle
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}