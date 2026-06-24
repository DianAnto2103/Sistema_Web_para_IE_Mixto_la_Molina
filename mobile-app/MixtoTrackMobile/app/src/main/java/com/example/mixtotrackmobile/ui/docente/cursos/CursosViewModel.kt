package com.example.mixtotrackmobile.ui.docente.cursos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.data.repository.DocenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CursosViewModel @Inject constructor(
    private val docenteRepository: DocenteRepository
) : ViewModel() {

    private val _cursos = MutableLiveData<List<Curso>>()
    val cursos: LiveData<List<Curso>> = _cursos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun cargarCursos(docenteId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = docenteRepository.listarCursosDocente(docenteId)

                if (result.isNotEmpty()) {
                    _cursos.value = result
                } else {
                    // Datos de ejemplo con el modelo REAL
                    _cursos.value = getCursosEjemplo()
                }

                _isLoading.value = false
            } catch (e: Exception) {
                _cursos.value = getCursosEjemplo()
                _errorMessage.value = e.message ?: "Error al cargar cursos"
                _isLoading.value = false
            }
        }
    }


    private fun getCursosEjemplo(): List<Curso> {
        return listOf(
            Curso(
                id = 1,
                nombre = "Matemáticas",
                descripcion = "Álgebra y geometría - 5to A",
                docente = 1
            ),
            Curso(
                id = 2,
                nombre = "Matemáticas",
                descripcion = "Álgebra y geometría - 5to B",
                docente = 1
            ),
            Curso(
                id = 3,
                nombre = "Matemáticas",
                descripcion = "Aritmética - 4to A",
                docente = 1
            )
        )
    }
}