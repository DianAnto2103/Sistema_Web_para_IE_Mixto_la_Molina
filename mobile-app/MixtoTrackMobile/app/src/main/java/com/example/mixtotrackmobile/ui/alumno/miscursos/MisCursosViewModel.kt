package com.example.mixtotrackmobile.ui.alumno.miscursos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.CursoResponse
import com.example.mixtotrackmobile.data.repository.CursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisCursosViewModel @Inject constructor(
    private val cursoRepository: CursoRepository
) : ViewModel() {

    private val _cursos = MutableLiveData<List<CursoResponse>>()
    val cursos: LiveData<List<CursoResponse>> = _cursos

    private val _promedioGeneral = MutableLiveData(0.0)
    val promedioGeneral: LiveData<Double> = _promedioGeneral

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarCursos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = cursoRepository.getMisCursos()

            result.fold(
                onSuccess = { lista ->
                    _cursos.value = lista
                    calcularPromedioGeneral(lista)
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    private fun calcularPromedioGeneral(cursos: List<CursoResponse>) {
        val promedios = cursos.mapNotNull { it.promedio }
        val promedio = if (promedios.isNotEmpty()) {
            promedios.average()
        } else 0.0
        _promedioGeneral.value = promedio
    }
}