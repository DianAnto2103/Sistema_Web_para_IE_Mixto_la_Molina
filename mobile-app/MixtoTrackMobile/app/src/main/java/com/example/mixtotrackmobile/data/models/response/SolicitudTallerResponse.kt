package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class SolicitudTallerResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("taller") val taller: Int,
    @SerializedName("taller_nombre") val tallerNombre: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("horario") val horario: String,
    @SerializedName("estado") val estado: String,  // "pendiente", "aprobado", "rechazado"
    @SerializedName("fecha_solicitud") val fechaSolicitud: String,
    @SerializedName("motivo") val motivo: String? = null
)