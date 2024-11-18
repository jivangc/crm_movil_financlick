package com.financlick.crm_financlick_movil.items

import java.util.Date

data class CardCampaniaItem (
    val idCampania: Int,
    val nombre: String,
    val asunto: String,
    val contenido: String,
    val createdDate: String,
    val scheduleDate: String,
    val tipo: String,
    val estatus: Int,
    val destinatarios: String,
    val idEmpresa: Int,
) { }