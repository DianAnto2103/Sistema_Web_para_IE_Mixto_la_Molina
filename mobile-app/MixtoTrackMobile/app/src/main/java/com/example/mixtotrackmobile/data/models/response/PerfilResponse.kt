package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class PerfilResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("email") val email: String,
    @SerializedName("rol") val rol: String,  // "ALUMNO" o "DOCENTE"

    // ===== CAMPOS DE ALUMNO =====
    @SerializedName("grado") val grado: String? = null,
    @SerializedName("seccion") val seccion: String? = null,
    @SerializedName("promedio_general") val promedioGeneral: Double? = null,

    // ===== CAMPOS DE DOCENTE =====
    @SerializedName("departamento") val departamento: String? = null,
    @SerializedName("especialidad") val especialidad: String? = null,
    @SerializedName("años_experiencia") val añosExperiencia: Int? = null
)