package com.example.mixtotrackmobile.data.models.request

data class SolicitudTallerRequest(
    val alumno: Int,
    val taller: Int,
    val estado: String = "pendiente"
)