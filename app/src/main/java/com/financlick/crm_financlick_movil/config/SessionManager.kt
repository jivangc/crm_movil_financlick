package com.financlick.crm_financlick_movil.config

import android.content.Context
import android.util.Log
import com.financlick.crm_financlick_movil.models.UsuarioModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException

class SessionManager(val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("financlick_crm", Context.MODE_PRIVATE)

    // Guardar Datos del Usuario
    fun saveUser(userRaw: JsonObject, token: String) {

        // Eliminar la sesión anterior si el usuario ya estaba logueado
        if (isLoggedIn()) {
            clearSession()
        }

        val editor = sharedPreferences.edit()

        val usuarioCliente = userRaw.get("usuarioCliente")?.asJsonObject
        editor.putString("user", usuarioCliente?.toString() ?: "{}")
        Log.d("saveUser", "User: ${usuarioCliente?.toString()}")

        val clienteObj = userRaw.get("cliente")?.asJsonObject
        editor.putString("cliente", clienteObj?.toString() ?: "{}")
        Log.d("saveUser", "Cliente: ${clienteObj?.toString()}")
        if (clienteObj != null) {
            val regimenFiscalStr = clienteObj.get("regimenFiscal")?.asString
            if (regimenFiscalStr != null) {
                if (regimenFiscalStr == "FISICA") {
                    editor.putString("user_type", "FISICA")
                    val personaObj = userRaw.get("persona")?.asJsonObject
                    editor.putString("person", personaObj?.toString() ?: "{}")
                    Log.d("saveUser", "Person (FISICA): ${personaObj?.toString()}")

                    val dataFisica = userRaw.get("datosClienteFisica")?.asJsonObject
                    editor.putString("data", dataFisica?.toString() ?: "{}")
                    Log.d("saveUser", "Data (FISICA): ${dataFisica?.toString()}")
                } else {
                    editor.putString("user_type", "MORAL")
                    val personaMoralObj = userRaw.get("personaMoral")?.asJsonObject
                    editor.putString("person", personaMoralObj?.toString() ?: "{}")
                    Log.d("saveUser", "Person (MORAL): ${personaMoralObj?.toString()}")

                    val dataMoral = userRaw.get("datosClienteMoral")?.asJsonObject
                    editor.putString("data", dataMoral?.toString() ?: "{}")
                    Log.d("saveUser", "Data (MORAL): ${dataMoral?.toString()}")
                }
            }
        }
        editor.putString("token", token)
        Log.d("saveUser", "Token: $token")

        editor.apply() // Usar apply() para guardar los cambios de forma asíncrona
    }

    // Obtener datos del usuario
    fun getUser(): UsuarioModel?{
        val json = sharedPreferences.getString("user", null)
        return Gson().fromJson(json, UsuarioModel::class.java)
    }
    fun getToken(): String?{
        return sharedPreferences.getString("token", null)
    }

    fun getUserType(): String?{
        return sharedPreferences.getString("user_type", null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("user") && sharedPreferences.contains("token")
    }

    // Eliminar Datos del Usuario (Cerrar Sesion)
    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}