package com.example.mixtotrackmobile.data.models.request

import com.google.gson.annotations.SerializedName

data class CrearTallerRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("curso") val curso: String,
    @SerializedName("horario") val horario: String,
    @SerializedName("cupos") val cupos: Int,
    @SerializedName("fecha_inicio") val fechaInicio: String,
    @SerializedName("fecha_fin") val fechaFin: String
)