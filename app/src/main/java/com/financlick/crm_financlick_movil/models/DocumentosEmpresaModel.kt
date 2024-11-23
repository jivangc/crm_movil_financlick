package com.financlick.crm_financlick_movil.models

import okhttp3.RequestBody

data class DocumentoEmpresaModel(
    val idDocumentoEmpresa: Int,
    val idEmpresa: Int,
    val idDocumento: Int,
    val estadoDocumento: String,
    val fechaSubida: String,
    val rutaArchivo: String?
)

data class DocumentoModel(
    val idDocumento: Int,
    val nombreDocumento: String,
    val descripcion: String?,
    val esObligatorio: Boolean
)

data class DocumentoUploadModel(
    val idEmpresa: Int,
    val idDocumento: Int,
    val archivo: RequestBody
)

data class DocumentoBase64Model(
    val base64: String,
    val fileName: String
)