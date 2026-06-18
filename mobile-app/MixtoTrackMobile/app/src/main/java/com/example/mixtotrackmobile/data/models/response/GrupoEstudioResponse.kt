package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class GrupoEstudioResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("curso") val curso: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("cupos") val cupos: Int,
    @SerializedName("cupos_disponibles") val cuposDisponibles: Int,
    @SerializedName("miembros") val miembros: List<MiembroResponse>,
    @SerializedName("creador") val creador: String,
    @SerializedName("es_creador") val esCreador: Boolean = false,
    @SerializedName("es_miembro") val esMiembro: Boolean = false
)

data class MiembroResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String
)