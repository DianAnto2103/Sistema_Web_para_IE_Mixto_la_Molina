package com.example.mixtotrackmobile.data.models.entities

data class PerfilData(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val rol: String,
    // Alumno
    val grado: String? = null,
    val seccion: String? = null,
    val promedioGeneral: Double? = null,
    // Docente
    val departamento: String? = null,
    val especialidad: String? = null,
    val añosExperiencia: Int? = null
)