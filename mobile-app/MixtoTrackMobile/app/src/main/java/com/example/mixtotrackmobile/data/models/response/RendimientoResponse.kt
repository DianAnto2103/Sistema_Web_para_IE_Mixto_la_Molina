package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class RendimientoResponse(
    @SerializedName("curso_id") val cursoId: Int,
    @SerializedName("curso_nombre") val cursoNombre: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("promedio_general") val promedioGeneral: Double,
    @SerializedName("bimestres") val bimestres: List<BimestreRendimiento>
)

data class BimestreRendimiento(
    @SerializedName("bimestre") val bimestre: Int,
    @SerializedName("nota") val nota: Double?,
    @SerializedName("estado") val estado: String,  // "Aprobado", "Desaprobado", "Pendiente"
    @SerializedName("fecha") val fecha: String?
)