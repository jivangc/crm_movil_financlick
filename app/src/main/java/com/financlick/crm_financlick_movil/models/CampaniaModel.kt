package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CampaniaModel(
    @SerializedName("idCampania") val idCampania: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("asunto") val asunto: String,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("scheduleDate") val scheduleDate: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("estatus") val estatus: Int,
    @SerializedName("destinatarios") val destinatarios: String,
    @SerializedName("idEmpresa") val idEmpresa: Int,
) {
}