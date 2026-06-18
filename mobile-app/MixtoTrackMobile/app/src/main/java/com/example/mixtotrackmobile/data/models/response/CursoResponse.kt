package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class CursoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("promedio") val promedio: Double? = null,
    @SerializedName("bimestres") val bimestres: List<BimestreCurso>? = null
)

data class BimestreCurso(
    @SerializedName("bimestre") val bimestre: Int,
    @SerializedName("nota") val nota: Double? = null,
    @SerializedName("estado") val estado: String? = null  // "Aprobado", "Desaprobado", "Pendiente"
)