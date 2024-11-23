package com.financlick.crm_financlick_movil.api

import com.financlick.crm_financlick_movil.models.CampaniaModel
import com.financlick.crm_financlick_movil.models.ActividadModel
import com.financlick.crm_financlick_movil.models.DocumentoBase64Model
import com.financlick.crm_financlick_movil.models.DocumentoEmpresaModel
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import com.financlick.crm_financlick_movil.models.LoginModel
import com.financlick.crm_financlick_movil.models.PlanEmpresaModel
import com.financlick.crm_financlick_movil.models.PlanificacionModel
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.financlick.crm_financlick_movil.models.QuejaRequestModel
import com.financlick.crm_financlick_movil.models.UsuarioModel
import com.financlick.crm_financlick_movil.models.VentasModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
interface ServiceApi {

    // ------------- ACCESO -------------
    @POST("Usuario/login")
    fun login(@Body loginModel: LoginModel): Call<ResponseBody>

    @GET("Usuario/detail")
    fun getUser(): Call<ResponseBody>

    @GET("Usuario/usuariosPorRol")
    fun getUsuariosPorRol(@Query("idRol") idRol: Int = 7): Call<List<UsuarioModel>>

    // ------------- QUEJAS Y SUGERENCIAS -------------
    // Obtener todas las quejas
    @GET("QuejaSugerencium")
    fun getQuejas(): Call<List<QuejaModel>>

    // Obtener una queja por ID
    @GET("QuejaSugerencium/{id}")
    fun getQueja(@Path("id") id: Int): Call<QuejaModel>

    // Crear una nueva queja
    @POST("QuejaSugerencium")
    fun createQueja(@Body queja: QuejaRequestModel): Call<ResponseBody>

    // Actualizar una queja existente
    @PUT("QuejaSugerencium/{id}")
    fun updateQueja(@Path("id") id: Int, @Body queja: QuejaModel): Call<Void>

    // Eliminar una queja
    @DELETE("QuejaSugerencium/{id}")
    fun deleteQueja(@Path("id") id: Int): Call<Void>

    // ------------- EMPRESAS -------------
    // Obtener todas las empresas
    @GET("Empresa")
    fun getEmpresas(): Call<List<EmpresaModel>>

    //Crear una nueva empresa
    @POST("Empresa")
    fun createEmpresa(@Body empresa: EmpresaModel): Call<ResponseBody>

    //Actualizar una empresa existente
    @PUT("Empresa/{id}")
    fun updateEmpresa(@Body empresa: EmpresaModel, @Path("id") id: Int): Call<Void>

    //Eliminar una empresa
    @DELETE("Empresa/{id}")
    fun deleteEmpresa(@Path("id") id: Int): Call<Void>

    // ------------- VENTAS -------------
    @GET("VentasProspecto")
    fun getVentas(): Call<List<VentasModel>>

    @GET("VentasProspecto")
    fun getVentasSinIngresos(): Call<List<VentasModel>>

    @GET("VentasProspecto/{id}")
    fun getVenta(@Path("id") id: Int): Call<VentasModel>

    @POST("VentasProspecto")
    fun createVenta(@Body venta: VentasModel): Call<ResponseBody>

    @PUT("VentasProspecto/{id}") // Ajusta a la ruta completa en el backend si `Venta` no es suficiente.
    fun updateVenta(@Path("id") id: Int, @Body fields: Map<String, Int>): Call<ResponseBody>

    @DELETE("VentasProspecto/{id}")
    fun deleteVenta(@Path("id") id: Int): Call<Void>

    @GET("VentasProspecto/pendientes")
    fun getVentasPendientes(): Call<List<VentasModel>>

    // Planificacion

    @GET("ContactoPersona")
    fun getContactos(): Call<List<PlanificacionModel>>

    @POST("contactopersona")
    fun createContacto(@Body ContactoPersona: PlanificacionModel): Call<PlanificacionModel>

    @PUT("contactopersona/{id}")
    fun updateContacto(@Path("id") id: Int, @Body ContactoPersona: PlanificacionModel): Call<Void>

    @DELETE("contactopersona/{id}")
    fun deleteContacto(@Path("id") id: Int): Call<Void>

    // INGRESOS Y EGRESOS
    @GET("IngresosEgreso")
    fun getIngresosEgresos(): Call<List<IngresosEgresoModel>>

    @GET("IngresosEgreso/ingresos")
    fun getIngresos(): Call<List<IngresosEgresoModel>>

    @POST("IngresosEgreso/ingresos")
    fun crearIngreso(
        @Body request: IngresosEgresoModel,
        @Query("idVentaProspecto") idVentaProspecto: Int
    ): Call<IngresosEgresoModel>

    // ServiceApi.kt
    @GET("PlanEmpresa/{idPlan}")
    fun getPlanById(@Path("idPlan") idPlan: Int): Call<PlanEmpresaModel>

    // ------------- Campanias -------------
    @GET("Campania")
    fun getCampanias(): Call<List<CampaniaModel>>

    @POST("Campania")
    fun createCampania(@Body campania: CampaniaModel): Call<ResponseBody>

    @GET("Campania/{id}")
    fun getCampania(@Path("id") id: Int): Call<CampaniaModel>

    @PUT("Campania/{id}")
    fun updateCampania(@Path("id") id: Int, @Body campania: CampaniaModel): Call<ResponseBody>

    @DELETE("Campania/{id}")
    fun deleteCampania(@Path("id") id: Int): Call<Void>

    @POST("Campania/{id}/sendMails")
    fun sendMails(@Path("id") id: Int, @Body emailsBody: EmailsBody): Call<Void>

    data class EmailsBody(
        val emails: List<String>
    )


    //Actividades

    @GET("actividad")
    fun getActividades(): Call<List<ActividadModel>>

    @GET("actividad/{id}")
    fun getActividad(@Path("id") id: Int): Call<ActividadModel>

    @GET("actividad/usuario/{idUsuario}")
    fun getActividadesByUsuario(@Path("idUsuario") idUsuario: Int): Call<List<ActividadModel>>

    @POST("actividad")
    fun createActividad(@Body Actividad: ActividadModel): Call<ActividadModel>

    @PUT("actividad/{id}")
    fun updateActividad(@Path("id") id: Int, @Body Actividad: ActividadModel): Call<Void>

    @DELETE("actividad/{id}")
    fun deleteActividad(@Path("id") id: Int): Call<Void>


    // DocumentosEmpresa endpoints
    @GET("documentos-empresa/lista/{idEmpresa}")
    fun getDocumentosEmpresa(@Path("idEmpresa") idEmpresa: Int): Call<List<DocumentoEmpresaModel>>

    @Multipart
    @POST("documentos-empresa/subir")
    fun subirDocumento(
        @Part("idEmpresa") idEmpresa: RequestBody,
        @Part("idDocumento") idDocumento: RequestBody,
        @Part archivo: MultipartBody.Part
    ): Call<Void>

    @Multipart
    @PUT("documentos-empresa/modificar/{idDocumentoEmpresa}")
    fun modificarDocumento(
        @Path("idDocumentoEmpresa") idDocumentoEmpresa: Int,
        @Part archivo: MultipartBody.Part
    ): Call<Void>

    @GET("documentos-empresa/descargar/{idDocumentoEmpresa}")
    fun descargarDocumento(@Path("idDocumentoEmpresa") idDocumentoEmpresa: Int): Call<DocumentoBase64Model>

    @DELETE("documentos-empresa/eliminar/{idDocumentoEmpresa}")
    fun eliminarDocumento(@Path("idDocumentoEmpresa") idDocumentoEmpresa: Int): Call<Void>



}