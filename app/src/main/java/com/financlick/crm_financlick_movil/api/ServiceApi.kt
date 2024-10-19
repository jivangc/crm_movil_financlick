package com.financlick.crm_financlick_movil.api

import com.financlick.crm_financlick_movil.models.LoginModel
import com.financlick.crm_financlick_movil.models.QuejaModel
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
    fun createQueja(@Body queja: QuejaModel): Call<ResponseBody>

    // Actualizar una queja existente
    @PUT("QuejaSugerencium/{id}")
    fun updateQueja(@Path("id") id: Int, @Body queja: QuejaModel): Call<Void>

    // Eliminar una queja
    @DELETE("QuejaSugerencium/{id}")
    fun deleteQueja(@Path("id") id: Int): Call<Void>
}