package com.financlick.crm_financlick_movil.api

import com.financlick.crm_financlick_movil.models.QuejaModel
import retrofit2.Call
import retrofit2.http.*
interface QuejaApi {

    // Obtener todas las quejas
    @GET("api/QuejaSugerencium")
    fun getQuejas(): Call<List<QuejaModel>>

    // Obtener una queja por ID
    @GET("api/QuejaSugerencium/{id}")
    fun getQueja(@Path("id") id: Int): Call<QuejaModel>

    // Crear una nueva queja
    @POST("api/QuejaSugerencium")
    fun createQueja(@Body queja: QuejaModel): Call<QuejaModel>

    // Actualizar una queja existente
    @PUT("api/QuejaSugerencium/{id}")
    fun updateQueja(@Path("id") id: Int, @Body queja: QuejaModel): Call<Void>

    // Eliminar una queja
    @DELETE("api/QuejaSugerencium/{id}")
    fun deleteQueja(@Path("id") id: Int): Call<Void>
}