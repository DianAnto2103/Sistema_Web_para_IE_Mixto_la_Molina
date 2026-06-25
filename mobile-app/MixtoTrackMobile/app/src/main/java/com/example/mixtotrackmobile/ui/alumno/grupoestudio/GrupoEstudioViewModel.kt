package com.example.mixtotrackmobile.ui.alumno.grupoestudio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixtotrackmobile.data.models.response.GrupoEstudioResponse
import com.example.mixtotrackmobile.data.repository.GrupoEstudioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrupoEstudioViewModel @Inject constructor(
    private val grupoRepository: GrupoEstudioRepository
) : ViewModel() {

    private val _grupos = MutableLiveData<List<GrupoEstudioResponse>>()
    val grupos: LiveData<List<GrupoEstudioResponse>> = _grupos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _mensaje = MutableLiveData<String?>()
    val mensaje: LiveData<String?> = _mensaje

    fun cargarGrupos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = grupoRepository.getGrupos()

            result.fold(
                onSuccess = { lista ->
                    _grupos.value = lista
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun crearGrupo(nombre: String, curso: String, descripcion: String, cupos: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensaje.value = null

            val result = grupoRepository.crearGrupo(nombre, curso, descripcion, cupos)

            result.fold(
                onSuccess = { grupo ->
                    _mensaje.value = "Grupo creado exitosamente"
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun unirseAGrupo(grupoId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensaje.value = null

            val result = grupoRepository.unirseAGrupo(grupoId)

            result.fold(
                onSuccess = {
                    _mensaje.value = "Te has unido al grupo"
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun salirDelGrupo(grupoId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensaje.value = null

            val result = grupoRepository.salirDelGrupo(grupoId)

            result.fold(
                onSuccess = {
                    _mensaje.value = "Has salido del grupo"
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }

    fun eliminarGrupo(grupoId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _mensaje.value = null

            val result = grupoRepository.eliminarGrupo(grupoId)

            result.fold(
                onSuccess = {
                    _mensaje.value = "Grupo eliminado"
                },
                onFailure = { exception ->
                    _error.value = exception.message
                }
            )

            _isLoading.value = false
        }
    }
}