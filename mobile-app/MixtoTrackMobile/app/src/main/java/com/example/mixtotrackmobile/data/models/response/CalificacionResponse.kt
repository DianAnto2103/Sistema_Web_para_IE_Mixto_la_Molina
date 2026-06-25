package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class CalificacionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("alumno_id") val alumnoId: Int,
    @SerializedName("curso_id") val cursoId: Int,
    @SerializedName("bimestre") val bimestre: Int,
    @SerializedName("nota") val nota: Double,
    @SerializedName("alumno_nombre") val alumnoNombre: String? = null,
    @SerializedName("curso_nombre") val cursoNombre: String? = null
)