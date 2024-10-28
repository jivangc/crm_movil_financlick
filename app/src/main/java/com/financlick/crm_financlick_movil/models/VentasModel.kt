package com.financlick.crm_financlick_movil.models
import java.io.Serializable
import com.google.gson.annotations.SerializedName

class VentasModel (
    @SerializedName("idVenta") val idVenta: Int,
    @SerializedName("idPlan") val idVPlan: Int, // Cambia temporalmente a "idPlan"
    @SerializedName("idUsuario") val idUsuario: Int?,
    @SerializedName("fechaSolicitud") val fechaSolicitud: String,
    @SerializedName("nombreCliente") val nombreCliente: String,
    @SerializedName("nombreEmpresa") val nombreEmpresa: String,
    @SerializedName("numeroContacto") val numeroContacto: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("domicilio") val domicilio: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("rfc") val rfc: String,
) : Serializable
