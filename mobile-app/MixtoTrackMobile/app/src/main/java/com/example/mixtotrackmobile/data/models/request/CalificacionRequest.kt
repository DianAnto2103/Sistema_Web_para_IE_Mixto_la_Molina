package com.example.mixtotrackmobile.data.models.request

data class CalificacionRequest(
    val alumno: Int,
    val curso: Int,
    val nota: Double,
    val bimestre: Int
)