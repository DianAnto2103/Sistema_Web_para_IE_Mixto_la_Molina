package com.example.mixtotrackmobile.data.models.entities

data class Taller(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val dia: String,
    val hora_inicio: String,
    val hora_fin: String,
    val cupos_totales: Int,
    val docente: Int
)