package com.example.mixtotrackmobile.data.model

data class SolicitudTallerResponse(
    val id: Int,
    val alumno: Int,
    val taller: Int,
    val fecha_solicitud: String,
    val estado: String
)
