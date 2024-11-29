package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.financlick.crm_financlick_movil.models.QuejaRequestModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuejaFormActivity : AppCompatActivity() {
    private lateinit var tipoInput: TextInputEditText
    private lateinit var descripcionInput: TextInputEditText
    private lateinit var prioridadSpinner: Spinner
    private lateinit var fechaRegistroInput: TextInputEditText
    private lateinit var estatusSpinner: Spinner
    private lateinit var fechaResolucionInput: TextInputEditText
    private lateinit var comentariosInput: TextInputEditText
    private lateinit var responsableInput: TextInputEditText
    private lateinit var empresaIdInput: TextInputEditText
    private lateinit var btnEnviar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private var contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_queja_form)
        var quejaRaw = intent.getStringExtra("queja").toString()
        val queja = Gson().fromJson(quejaRaw, CardQuejaItem::class.java)

        // Inicializar vistas
        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)


        tipoInput = findViewById(R.id.tipoInput)
        descripcionInput = findViewById(R.id.descripcionInput)
        prioridadSpinner = findViewById(R.id.prioridadSpinner)
        fechaRegistroInput = findViewById(R.id.fechaRegistroInput)
        estatusSpinner = findViewById(R.id.estatusSpinner)
        fechaResolucionInput = findViewById(R.id.fechaResolucionInput)
        comentariosInput = findViewById(R.id.comentariosInput)
        responsableInput = findViewById(R.id.responsableInput)
        empresaIdInput = findViewById(R.id.empresaIdInput)

        btnEnviar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)

        queja?.let {
            tipoInput.setText(it.tipo)
            descripcionInput.setText(it.descripcion)
            fechaRegistroInput.setText(it.fechaRegistro)
            fechaResolucionInput.setText(it.fechaResolucion ?: "")
            comentariosInput.setText(it.comentarios ?: "")
            responsableInput.setText(it.responsable?.toString() ?: "")
            empresaIdInput.setText(it.idEmpresa.toString())

            // Seleccionar la prioridad adecuada en el Spinner
            val prioridadIndex = when (it.prioridad) {
                1 -> 0 // Baja
                2 -> 1 // Media
                3 -> 2 // Alta
                else -> 0
            }
            prioridadSpinner.setSelection(prioridadIndex)

            // Seleccionar el estatus adecuado en el Spinner
            val estatusIndex = when (it.estatus) {
                1 -> 0 // Abierto
                2 -> 1 // En proceso
                3 -> 2 // Resuelto
                4 -> 3 // Cerrado
                else -> 0
            }
            estatusSpinner.setSelection(estatusIndex)
        }

        btnEnviar.setOnClickListener() {
            val request = loadQuejaRequest()
            guardarQueja(request)
//            if (queja.idQuejaSugerencia == 0) {
//                // Guardar Nuevo
//                Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show()
//
//            }else{
//                // Actualizar
//                Toast.makeText(this, "Actualizacion", Toast.LENGTH_SHORT).show()
//                actualizarQueja(request)
//            }
        }

        btnCancelar.setOnClickListener() {
            finish()
        }
    }

    private fun loadQuejaRequest(): QuejaRequestModel {
        val queja = QuejaRequestModel(
            idQuejaSugerencia = 0,
            idEmpresa = empresaIdInput.text.toString().toInt(),
            tipo = tipoInput.text.toString(),
            descripcion = descripcionInput.text.toString(),
            fechaRegistro = fechaRegistroInput.text.toString(),
            estado = estatusSpinner.selectedItemPosition + 1,
            fechaResolucion = fechaResolucionInput.text.toString(),
            responsable = responsableInput.text.toString().toInt(),
            prioridad = prioridadSpinner.selectedItemPosition + 1,
            comentarios = comentariosInput.text.toString()
        )

        return queja
    }

    private fun guardarQueja(param: QuejaRequestModel) {
        RetrofitClient.instance.createQueja(param).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(contexto, "Documento subido exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(contexto, "Error en la respuesta: $error", Toast.LENGTH_SHORT).show()
                    Log.i("Error", error ?: "No se pudo obtener el mensaje de error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.i("Error", call.toString())
            }
        })
    }

    private fun actualizarQueja(param: QuejaModel) {

    }
}