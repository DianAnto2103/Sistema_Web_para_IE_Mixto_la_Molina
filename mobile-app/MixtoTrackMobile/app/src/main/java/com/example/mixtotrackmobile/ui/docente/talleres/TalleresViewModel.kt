package com.example.mixtotrackmobile.ui.docente.talleres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.TallerDocenteResponse
import com.example.mixtotrackmobile.data.repository.TallerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TalleresViewModel @Inject constructor(
    private val tallerRepository: TallerRepository
) : ViewModel() {

    private val _talleres = MutableLiveData<List<TallerDocenteResponse>>()
    val talleres: LiveData<List<TallerDocenteResponse>> = _talleres

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    fun cambiarEstadoTaller(tallerId: Int, estado: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensaje.value = null

            val result = tallerRepository.cambiarEstadoTaller(tallerId, estado)

            result.fold(
                onSuccess = {
                    _mensaje.value = "Estado actualizado a: $estado"
                    cargarTalleres() // Recargar lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun eliminarTaller(tallerId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensaje.value = null

            val result = tallerRepository.eliminarTaller(tallerId)

            result.fold(
                onSuccess = {
                    _mensaje.value = "Taller eliminado correctamente"
                    cargarTalleres() // Recargar lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun cargarTalleres() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = tallerRepository.getTalleresDocente()

            result.fold(
                onSuccess = { lista ->
                    _talleres.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}