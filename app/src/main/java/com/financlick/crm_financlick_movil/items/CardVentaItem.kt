package com.financlick.crm_financlick_movil.items

import java.io.Serializable

data class CardVentaItem(
    val idVenta: Int,
    val idVPlan: Int,
    val idUsuario: Int?,
    val fechaSolicitud: String,
    val nombreCliente: String,
    val nombreEmpresa: String,
    val numeroContacto: String,
    val correo: String,
    val domicilio: String,
    val ciudad: String,
    val estado: String,
    val rfc: String
) : Serializable
