package com.example.mixtotrackmobile.ui.alumno.solicitar_taller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.TallerResponse
import com.example.mixtotrackmobile.data.repository.TallerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SolicitarTallerViewModel @Inject constructor(
    private val tallerRepository: TallerRepository
) : ViewModel() {

    private val _talleres = MutableLiveData<List<TallerResponse>>()
    val talleres: LiveData<List<TallerResponse>> = _talleres

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _mensajeExito = MutableLiveData<String?>()
    val mensajeExito: LiveData<String?> = _mensajeExito

    fun cargarTalleres() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = tallerRepository.getTalleres()

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

    fun solicitarTaller(tallerId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensajeExito.value = null

            val result = tallerRepository.solicitarTaller(tallerId)

            result.fold(
                onSuccess = { solicitud ->
                    _mensajeExito.value = "Taller solicitado exitosamente"
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}