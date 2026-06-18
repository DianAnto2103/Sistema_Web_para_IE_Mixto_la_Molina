package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName

data class NotificacionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("leida") val leida: Boolean,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("icono") val icono: String? = null
)