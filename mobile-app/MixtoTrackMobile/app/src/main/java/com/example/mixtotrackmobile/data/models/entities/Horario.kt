package com.example.mixtotrackmobile.data.models.entities

data class Horario(
    val id: Int,
    val docente: Int,
    val dia: String,
    val hora_inicio: String,
    val hora_fin: String,
    val curso: Int?
)
