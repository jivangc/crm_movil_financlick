package com.financlick.crm_financlick_movil.models


data class QuejaRequestModel (
    val idQuejaSugerencia: Int,
    val idEmpresa: Int,
    val tipo: String,
    val descripcion: String,
    val fechaRegistro: String,
    val estado: Int,
    val fechaResolucion: String? = null,
    val responsable: Int? = 0,
    val prioridad: Int? = 0,
    val comentarios: String? = null,
    val archivoAdjunto: String? = ""
){
}