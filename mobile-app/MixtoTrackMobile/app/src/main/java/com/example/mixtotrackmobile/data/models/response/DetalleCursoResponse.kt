package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class DetalleCursoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("docente") val docente: String,
    @SerializedName("docente_email") val docenteEmail: String?,
    @SerializedName("horario") val horario: String?,
    @SerializedName("aula") val aula: String?,
    @SerializedName("promedio") val promedio: Double?,
    @SerializedName("bimestres") val bimestres: List<BimestreDetalle>,
    @SerializedName("descripcion") val descripcion: String?
)

data class BimestreDetalle(
    @SerializedName("bimestre") val bimestre: Int,
    @SerializedName("nota") val nota: Double?,
    @SerializedName("estado") val estado: String,  // "Aprobado", "Desaprobado", "Pendiente"
    @SerializedName("fecha_inicio") val fechaInicio: String?,
    @SerializedName("fecha_fin") val fechaFin: String?
)