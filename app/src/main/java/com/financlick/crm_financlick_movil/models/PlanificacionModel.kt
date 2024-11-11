package com.financlick.crm_financlick_movil.models
import com.google.gson.annotations.SerializedName

data class PlanificacionModel (
    @SerializedName("idContacto") val idContacto: Int,
    @SerializedName("idEmpresa") val idEmpresa: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("puesto") val puesto: String? = null
){

}