package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CampaniaModel(
    @SerializedName("idCampania") val idCampania: Int,
    @SerializedName("tipo") val tipoCampania: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("dominioCampania") val dominioCampania: String,
    @SerializedName("fechaInicio") val fechaInicio: Date,
    @SerializedName("fechaFin") val fechaFin: Date,
    @SerializedName("contenido") val contenido: Date,
    @SerializedName("adjunto") val adjunto: String,
    @SerializedName("estado") val status: String,
) {
}