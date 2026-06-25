package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class AlumnoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("email") val email: String,
    @SerializedName("grado") val grado: String,
    @SerializedName("seccion") val seccion: String,
    @SerializedName("promedio_general") val promedioGeneral: Double? = null
)