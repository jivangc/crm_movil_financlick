// IngresoEgresoRequest.kt
package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName

data class IngresoEgresoRequest(
    @SerializedName("ingresoEgreso") val ingresoEgreso: IngresosEgresoModel
)
