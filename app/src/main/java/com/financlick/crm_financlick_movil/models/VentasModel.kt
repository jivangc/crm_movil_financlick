package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName

class VentasModel (
    @SerializedName("idVenta") val idVenta: Int,
    @SerializedName("idVPlan") val idVPlan: Int,
    @SerializedName("fechaSolicitud") val fechaSolicitud: String,
    @SerializedName("nombreCliente") val nombreCliente: String,
    @SerializedName("nombreEmpresa") val nombreEmpresa: String,
    @SerializedName("numeroContacto") val numeroContacto: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("domicilio") val domicilio: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("rfc") val rfc: String,
)