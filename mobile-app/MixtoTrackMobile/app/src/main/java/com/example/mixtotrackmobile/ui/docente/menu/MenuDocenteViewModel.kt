package com.example.mixtotrackmobile.ui.docente.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.repository.DocenteRepository
import com.example.mixtotrackmobile.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDocenteViewModel @Inject constructor(
    private val docenteRepository: DocenteRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _cursosCount = MutableLiveData(0)
    val cursosCount: LiveData<Int> = _cursosCount

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun cargarCursosCount() {
        val docenteId = sessionManager.getDocenteId()
        if (docenteId == -1) {
            _errorMessage.value = "No se pudo obtener el ID del docente"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Si tienes el método en DocenteRepository
                val result = docenteRepository.listarCursosDocente(docenteId)
                _cursosCount.value = result.size
                _isLoading.value = false
            } catch (e: Exception) {
                // Si NO tienes el método, usa un valor por defecto
                _cursosCount.value = 0
                _errorMessage.value = e.message ?: "Error al cargar cursos"
                _isLoading.value = false
            }
        }
    }
}