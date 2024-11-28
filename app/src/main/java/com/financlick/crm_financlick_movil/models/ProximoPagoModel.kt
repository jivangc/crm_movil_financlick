package com.financlick.crm_financlick_movil.models

import com.google.gson.annotations.SerializedName

data class ProximoPagoModel(
    @SerializedName("id") val id: Int,
    @SerializedName("amount") val amount: Double, // Usar Double para manejar decimales en Kotlin
    @SerializedName("stripePaymentIntentId") val stripePaymentIntentId: String?,
    @SerializedName("status") val status: String, // Valores: "Pending", "Paid", "Failed"
    @SerializedName("idEmpresa") val idEmpresa: Int,
    @SerializedName("createdAt") val createdAt: String, // Fecha en formato ISO 8601
    @SerializedName("paidAt") val paidAt: String? // Fecha en formato ISO 8601, nullable
)
