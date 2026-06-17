package com.example.mixtotrackmobile.data.models.entities

data class Alumno(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val fecha_nacimiento: String?,
    val grado: Int?,
    val seccion: String?
)
