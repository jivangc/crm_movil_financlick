// IngresosEgresoModel.kt
package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName

data class IngresosEgresoModel(
    @SerializedName("fecha") val fecha: String,
    @SerializedName("tipoTransaccion") val tipoTransaccion: Int,
    @SerializedName("monto") val monto: Double,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("estatus") val estatus: Int,
    @SerializedName("idEmpresa") val idEmpresa: Int?
)
