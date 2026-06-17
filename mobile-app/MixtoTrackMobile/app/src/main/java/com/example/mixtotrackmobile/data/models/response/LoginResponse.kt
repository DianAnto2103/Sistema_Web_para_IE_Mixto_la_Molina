package com.example.mixtotrackmobile.data.models.response

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("access") val access: String,
    @SerializedName("refresh") val refresh: String,
    @SerializedName("rol") val rol: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("apellido") val apellido: String?,
    @SerializedName("grado") val grado: String?
)