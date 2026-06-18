package com.example.mixtotrackmobile.data.models.request

import com.google.gson.annotations.SerializedName

data class CrearGrupoRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("curso") val curso: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("cupos") val cupos: Int
)