package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class TallerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("horario") val horario: String,
    @SerializedName("cupos") val cupos: Int,
    @SerializedName("cupos_disponibles") val cuposDisponibles: Int,
    @SerializedName("estado") val estado: String,  // "disponible", "completo"
    @SerializedName("fecha_inicio") val fechaInicio: String,
    @SerializedName("fecha_fin") val fechaFin: String
)

data class SolicitudTallerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("taller") val taller: Int,
    @SerializedName("alumno") val alumno: Int,
    @SerializedName("estado") val estado: String,  // "pendiente", "aprobado", "rechazado"
    @SerializedName("fecha_solicitud") val fechaSolicitud: String
)