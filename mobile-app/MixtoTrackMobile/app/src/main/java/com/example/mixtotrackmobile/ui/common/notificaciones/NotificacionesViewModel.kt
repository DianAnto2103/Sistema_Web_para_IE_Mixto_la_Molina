package com.example.mixtotrackmobile.ui.common.notificaciones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.NotificacionResponse
import com.example.mixtotrackmobile.data.repository.NotificacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificacionesViewModel @Inject constructor(
    private val notificacionRepository: NotificacionRepository
) : ViewModel() {

    private val _notificaciones = MutableLiveData<List<NotificacionResponse>>()
    val notificaciones: LiveData<List<NotificacionResponse>> = _notificaciones

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _contadorNoLeidas = MutableLiveData(0)
    val contadorNoLeidas: LiveData<Int> = _contadorNoLeidas

    private val _actualizarLista = MutableLiveData(false)
    val actualizarLista: LiveData<Boolean> = _actualizarLista

    fun cargarNotificaciones() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = notificacionRepository.getNotificaciones()

            result.fold(
                onSuccess = { lista ->
                    _notificaciones.value = lista
                    _contadorNoLeidas.value = lista.count { !it.leida }
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun marcarComoLeida(id: Int) {
        viewModelScope.launch {
            val result = notificacionRepository.marcarComoLeida(id)

            result.fold(
                onSuccess = {
                    _actualizarLista.value = true
                    _actualizarLista.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )
        }
    }

    fun marcarTodasComoLeidas() {
        viewModelScope.launch {
            val result = notificacionRepository.marcarTodasComoLeidas()

            result.fold(
                onSuccess = {
                    _actualizarLista.value = true
                    _actualizarLista.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )
        }
    }
}