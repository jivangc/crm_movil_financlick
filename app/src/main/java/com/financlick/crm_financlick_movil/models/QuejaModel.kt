package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName

data class QuejaModel(
    @SerializedName("idQuejaSugerencia") val idQuejaSugerencia: Int,
    @SerializedName("idEmpresa") val idEmpresa: Int,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fechaRegistro") val fechaRegistro: String,
    @SerializedName("estatus") val estado: Int,
    @SerializedName("fechaResolucion") val fechaResolucion: String? = null,
    @SerializedName("responsable") val responsable: Int? = 0,
    @SerializedName("prioridad") val prioridad: Int? = 0,
    @SerializedName("comentarios") val comentarios: String? = null,
    @SerializedName("archivoAdjunto") val archivoAdjunto: String? = ""
) {

}
