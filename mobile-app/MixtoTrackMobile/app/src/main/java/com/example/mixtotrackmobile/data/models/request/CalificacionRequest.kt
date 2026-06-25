package com.example.mixtotrackmobile.data.models.request

import com.google.gson.annotations.SerializedName

data class CalificacionRequest(
    @SerializedName("alumno_id") val alumnoId: Int,
    @SerializedName("curso_id") val cursoId: Int,
    @SerializedName("bimestre") val bimestre: Int,
    @SerializedName("nota") val nota: Double
)