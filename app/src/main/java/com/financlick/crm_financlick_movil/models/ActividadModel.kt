package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ActividadModel (
    @SerializedName("idActividad") val idActividad: Int,
    @SerializedName("idUsuario") val idUsuario: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("estatus") val estatus: String? = null
)
