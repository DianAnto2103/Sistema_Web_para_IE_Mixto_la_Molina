package com.example.mixtotrackmobile.ui.alumno.mis_cursos.rendimiento_academico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.RendimientoResponse
import com.example.mixtotrackmobile.data.repository.RendimientoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RendimientoAcademicoViewModel @Inject constructor(
    private val rendimientoRepository: RendimientoRepository
) : ViewModel() {

    private val _rendimiento = MutableLiveData<RendimientoResponse?>()
    val rendimiento: LiveData<RendimientoResponse?> = _rendimiento

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarRendimiento(cursoId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = rendimientoRepository.getRendimiento(cursoId)

            result.fold(
                onSuccess = { data ->
                    _rendimiento.value = data
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}