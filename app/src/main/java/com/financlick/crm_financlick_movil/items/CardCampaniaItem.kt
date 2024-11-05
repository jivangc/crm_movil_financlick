package com.financlick.crm_financlick_movil.items

import java.util.Date

data class CardCampaniaItem (
    val idCampania: Int,
    val tipoCampania: String,
    val titulo: String,
    val descripcion: String,
    val dominioCampania: String,
    val fechaInicio: Date,
    val fechaFin: Date?,
    val contenido: String,
    val adjunto: String,
    val status: String,
){
}