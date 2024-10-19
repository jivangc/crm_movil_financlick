package com.financlick.crm_financlick_movil.items

data class CardQuejaItem(
    val idQuejaSugerencia: Int,
    val idEmpresa: Int,
    val tipo: String,
    val descripcion: String,
    val fechaRegistro: String,
    val estatus: Int,
    val fechaResolucion: String?,
    val responsable: Int?,
    val prioridad: Int?,
    val comentarios: String?,
    val archivoAdjunto: String?
)
