package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class DetalleCursoDocenteResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("grado") val grado: String,
    @SerializedName("seccion") val seccion: String,
    @SerializedName("horario") val horario: String?,
    @SerializedName("aula") val aula: String?,
    @SerializedName("alumnos") val alumnos: List<AlumnoCursoDetalle>
)

data class AlumnoCursoDetalle(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("email") val email: String,
    @SerializedName("bimestres") val bimestres: List<CalificacionBimestre>
)

data class CalificacionBimestre(
    @SerializedName("bimestre") val bimestre: Int,
    @SerializedName("nota") val nota: Double?,
    @SerializedName("estado") val estado: String?  // "Aprobado", "Desaprobado", "Pendiente"
)