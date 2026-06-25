package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class TallerDocenteResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("curso") val curso: String,
    @SerializedName("horario") val horario: String,
    @SerializedName("cupos") val cupos: Int,
    @SerializedName("cupos_disponibles") val cuposDisponibles: Int,
    @SerializedName("solicitudes") val solicitudes: Int,
    @SerializedName("estado") val estado: String,  // "activo", "inactivo", "completo"
    @SerializedName("fecha_creacion") val fechaCreacion: String
)