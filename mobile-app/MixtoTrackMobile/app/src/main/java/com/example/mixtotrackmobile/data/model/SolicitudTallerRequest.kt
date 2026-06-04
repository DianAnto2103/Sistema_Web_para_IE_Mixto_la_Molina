package com.example.mixtotrackmobile.data.model

data class SolicitudTallerRequest(
    val alumno: Int,
    val taller: Int,
    val estado: String = "pendiente"
)
