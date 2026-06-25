package com.example.mixtotrackmobile.ui.docente.cursos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.CursoDocenteResponse
import com.example.mixtotrackmobile.data.repository.DocenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CursosViewModel @Inject constructor(
    private val docenteRepository: DocenteRepository
) : ViewModel() {

    private val _cursos = MutableLiveData<List<CursoDocenteResponse>>()
    val cursos: LiveData<List<CursoDocenteResponse>> = _cursos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarCursos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = docenteRepository.getCursosDocente()

            result.fold(
                onSuccess = { lista ->
                    _cursos.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}