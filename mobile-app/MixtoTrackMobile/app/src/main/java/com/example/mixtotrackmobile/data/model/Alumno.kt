package com.example.mixtotrackmobile.data.model

data class Alumno(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val fecha_nacimiento: String?,
    val grado: Int?,
    val seccion: String?
)
