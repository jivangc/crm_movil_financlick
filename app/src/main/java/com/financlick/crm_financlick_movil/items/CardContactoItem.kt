package com.financlick.crm_financlick_movil.items

data class CardContactoItem (
    val idContacto: Int,
    val idEmpresa: Int,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val email: String,
    val puesto: String?
    ){
}