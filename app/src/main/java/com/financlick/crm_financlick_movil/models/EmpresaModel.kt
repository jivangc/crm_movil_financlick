package com.financlick.crm_financlick_movil.models;
import com.google.gson.annotations.SerializedName;
import java.util.Date

data class EmpresaModel (
    @SerializedName("idEmpresa") val idEmpresa: Int,
    @SerializedName("nombreEmpresa") val nombreEmpresa: String,
    @SerializedName("razonSocial") val razonSocial: String,
    @SerializedName("fechaConstitucion") val fechaConstitucion: Date,
    @SerializedName("numeroEscritura") val numeroEscritura: String,
    @SerializedName("nombreNotario") val nombreNotario: String,
    @SerializedName("numeroNotario") val numeroNotario: String,
    @SerializedName("folioMercantil") val folioMercantil: String,
    @SerializedName("rfc") val rfc: String,
    @SerializedName("nombreRepresentanteLegal") val nombreRepresentanteLegal: String,
    @SerializedName("numeroEscrituraRepLeg") val numeroEscrituraRepLeg: String,
    @SerializedName("fechaInscripcion") val fechaInscripcion: Date,
    @SerializedName("calle") val calle: String,
    @SerializedName("colonia") val colonia: String,
    @SerializedName("cp") val cp: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("estado") val estado: String,
    @SerializedName("localidad") val localidad: String,
    @SerializedName("numExterior") val numExterior: String,
    @SerializedName("numInterior") val numInterior: String,
    @SerializedName("email") val email: String,
    @SerializedName("estatus") val estatus: Int,
    @SerializedName("logo") val logo: String,
)
{

}