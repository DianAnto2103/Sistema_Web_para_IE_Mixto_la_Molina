package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class CursoDocenteResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("grado") val grado: String,
    @SerializedName("seccion") val seccion: String,
    @SerializedName("cantidad_alumnos") val cantidadAlumnos: Int,
    @SerializedName("promedio_general") val promedioGeneral: Double? = null,
    @SerializedName("horario") val horario: String? = null,
    @SerializedName("aula") val aula: String? = null
)