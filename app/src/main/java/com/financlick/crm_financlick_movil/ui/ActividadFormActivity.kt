package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.ActividadModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActividadFormActivity : AppCompatActivity() {

    private lateinit var nombreInput: TextInputEditText
    private lateinit var descripcionInput: TextInputEditText
    private lateinit var fechaCreacionInput: TextInputEditText
    private lateinit var responsableInput: TextInputEditText
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private var actividad: ActividadModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_form)

        // Inicializar vistas
        nombreInput = findViewById(R.id.nombreInput)
        descripcionInput = findViewById(R.id.descripcionInput)
        fechaCreacionInput = findViewById(R.id.fechaCreacionInput)
        responsableInput = findViewById(R.id.responsableInput)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Cargar actividad desde el Intent si existe
        val actividadRaw = intent.getStringExtra("actividad")
        actividad = if (!actividadRaw.isNullOrEmpty()) {
            Gson().fromJson(actividadRaw, ActividadModel::class.java).also {
                llenarCampos(it)
            }
        } else {
            null
        }

        // Configurar el botón de guardar
        btnGuardar.setOnClickListener {
            val actividadRequest = cargarActividadRequest()
            if (actividad == null) {
                guardarActividad(actividadRequest)
            } else {
                actualizarActividad(actividadRequest)
            }
        }

        // Configurar el botón de cancelar
        btnCancelar.setOnClickListener {
            val intent = Intent(this, ActividadActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun callCards() {
        val intent = Intent(this, ActividadActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun llenarCampos(actividad: ActividadModel) {
        nombreInput.setText(actividad.nombre)
        descripcionInput.setText(actividad.descripcion ?: "")
        responsableInput.setText(actividad.idUsuario.toString())
    }

    private fun cargarActividadRequest(): ActividadModel {
        return ActividadModel(
            idActividad = actividad?.idActividad ?: 0,
            idUsuario = responsableInput.text.toString().toIntOrNull() ?: 0,
            nombre = nombreInput.text.toString(),
            descripcion = descripcionInput.text.toString(),
            estatus = actividad?.estatus

        )
    }

    private fun guardarActividad(actividad: ActividadModel) {
        RetrofitClient.instance.createActividad(actividad).enqueue(object : Callback<ActividadModel> {
            override fun onResponse(call: Call<ActividadModel>, response: Response<ActividadModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ActividadFormActivity, "Actividad guardada exitosamente", Toast.LENGTH_SHORT).show()
                    callCards()
                } else {
                    Toast.makeText(this@ActividadFormActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ActividadModel>, t: Throwable) {
                Toast.makeText(this@ActividadFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarActividad(actividad: ActividadModel) {
        RetrofitClient.instance.updateActividad(actividad.idActividad, actividad)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ActividadFormActivity, "Actividad actualizada exitosamente", Toast.LENGTH_SHORT).show()
                        callCards()
                    } else {
                        Toast.makeText(this@ActividadFormActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ActividadFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
