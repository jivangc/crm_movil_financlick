package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class IngresoEgresoModel(
    @SerializedName("idIngresosEgresos") val idIngresosEgresos: Int,
    @SerializedName("fecha") val fecha: Date? = null,
    @SerializedName("tipoTransaccion") val tipoTransaccion: Int,
    @SerializedName("monto") val monto: Double,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("categoria") val categoria: String,
    @SerializedName("estatus") val estatus: Int
)
{

}