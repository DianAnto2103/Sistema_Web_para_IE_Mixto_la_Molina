package com.example.mixtotrackmobile.data.models.request
import com.google.gson.annotations.SerializedName

data class SolicitudTallerRequest(
    @SerializedName("taller_id") val tallerId: Int,
    @SerializedName("alumno_id") val alumnoId: Int
)