package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class HorarioResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("docente") val docente: String,
    @SerializedName("docente_id") val docenteId: Int,
    @SerializedName("dia") val dia: String,  // "Lunes", "Martes", etc.
    @SerializedName("hora_inicio") val horaInicio: String,
    @SerializedName("hora_fin") val horaFin: String,
    @SerializedName("disponible") val disponible: Boolean,
    @SerializedName("taller") val taller: String? = null
)