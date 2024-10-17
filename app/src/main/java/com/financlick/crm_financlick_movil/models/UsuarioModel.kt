package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName

open class UsuarioModel(
    @SerializedName("idUsuario") val idUsuario: Int,
    @SerializedName("idRol") val idRol: Int,
    @SerializedName("contrasenia") val contrasenia: String,
    @SerializedName("apellidoPaterno") val apellidoPaterno: String,
    @SerializedName("apellidoMaterno") val apellidoMaterno: String,
    @SerializedName("idEmpresa") val idEmpresa: Int,
    @SerializedName("usuario1") val usuario1: String,
    @SerializedName("nombre") val nombre: String
) {
}