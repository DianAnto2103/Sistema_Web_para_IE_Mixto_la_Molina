package com.example.mixtotrackmobile.data.models.entities

data class Curso(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val docente: Int,
    val docenteNombre: String? = null,
    val promedio: Double? = null,
    val bimestres: List<Double>? = null
)
