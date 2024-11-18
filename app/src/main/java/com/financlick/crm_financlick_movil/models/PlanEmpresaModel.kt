// PlanEmpresaModel.kt
package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlanEmpresaModel(
    @SerializedName("idPlan") val idPlan: Int,
    @SerializedName("precio") val precio: Double,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("duracion") val duracion: String,
    @SerializedName("estatus") val estatus: Int
) : Serializable
