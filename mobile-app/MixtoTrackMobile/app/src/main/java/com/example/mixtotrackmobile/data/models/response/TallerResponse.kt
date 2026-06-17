package com.example.mixtotrackmobile.data.models.response

data class TallerResponse(
    val id: Int,
    val alumno: Int,
    val taller: Int,
    val fecha_solicitud: String,
    val estado: String
)