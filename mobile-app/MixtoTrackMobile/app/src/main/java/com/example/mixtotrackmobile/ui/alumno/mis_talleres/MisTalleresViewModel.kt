package com.example.mixtotrackmobile.ui.alumno.mis_talleres

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.SolicitudTallerResponse
import com.example.mixtotrackmobile.data.repository.TallerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MisTalleresViewModel @Inject constructor(
    private val tallerRepository: TallerRepository
) : ViewModel() {

    private val _solicitudes = MutableLiveData<List<SolicitudTallerResponse>>()
    val solicitudes: LiveData<List<SolicitudTallerResponse>> = _solicitudes

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun cargarSolicitudes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = tallerRepository.getMisSolicitudes()

            result.fold(
                onSuccess = { lista ->
                    _solicitudes.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}