package com.example.mixtotrackmobile.ui.docente.cursosdetalle_curso

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.DetalleCursoDocenteResponse
import com.example.mixtotrackmobile.data.repository.CursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleCursoDocenteViewModel @Inject constructor(
    private val cursoRepository: CursoRepository
) : ViewModel() {

    private val _detalleCurso = MutableLiveData<DetalleCursoDocenteResponse?>()
    val detalleCurso: LiveData<DetalleCursoDocenteResponse?> = _detalleCurso

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarDetalleCurso(cursoId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = cursoRepository.getDetalleCursoDocente(cursoId)

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