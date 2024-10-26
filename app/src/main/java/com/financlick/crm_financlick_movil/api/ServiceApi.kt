package com.financlick.crm_financlick_movil.api

import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.LoginModel
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.financlick.crm_financlick_movil.models.QuejaRequestModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
interface ServiceApi {

    // ------------- ACCESO -------------
    @POST("Usuario/login")
    fun login(@Body loginModel: LoginModel): Call<ResponseBody>

    @GET("Usuario/detail")
    fun getUser(): Call<ResponseBody>

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
    fun createEmpresa(@Body empresa: EmpresaModel): Call<EmpresaModel>

    //Actualizar una empresa existente
    @PUT("Empresa/{idEmpresa}")
    fun updateEmpresa(@Path("idEmpresa") idEmpresa: Int, @Body empresa: EmpresaModel): Call<Void>

    //Eliminar una empresa
    @DELETE("Empresa/{idEmpresa}")
    fun deleteEmpresa(@Path("id") id: Int): Call<Void>
}