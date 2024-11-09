package com.financlick.crm_financlick_movil.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardCampaniaItem
import com.financlick.crm_financlick_movil.models.CampaniaModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CampaniaFormActivity : AppCompatActivity() {
    private lateinit var tipoCampaniaInput: TextInputEditText
    private lateinit var tituloCampaniaInput: TextInputEditText
    private lateinit var descripcionCampaniaInput: TextInputEditText
    private lateinit var dominioCampaniaInput: TextInputEditText
    private lateinit var fechaInicioInput: TextInputEditText
    private lateinit var fechaFinInput: TextInputEditText
    private lateinit var contenidoInput: TextInputEditText
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_campania_form)
        var campaniaRaw = intent.getStringExtra("campania").toString()
        val campania = Gson().fromJson(campaniaRaw, CardCampaniaItem::class.java)

        tipoCampaniaInput = findViewById(R.id.tipoCampaniaInput)
        tituloCampaniaInput = findViewById(R.id.tituloCampaniaInput)
        descripcionCampaniaInput = findViewById(R.id.descripcionCampaniaInput)
        dominioCampaniaInput = findViewById(R.id.dominioCampaniaInput)
        fechaInicioInput = findViewById(R.id.fechaInicioInput)
        fechaFinInput = findViewById(R.id.fechaFinInput)
        contenidoInput = findViewById(R.id.contenidoInput)

        btnGuardar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Formato de fecha
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

        // Manejador para fecha de inicio
        fechaInicioInput.setOnClickListener {
            showDatePickerDialog(fechaInicioInput, dateFormat)
        }

        // Manejador para fecha de fin
        fechaFinInput.setOnClickListener {
            showDatePickerDialog(fechaFinInput, dateFormat)
        }

        campania?.let {
            tipoCampaniaInput.setText(it.tipo)
            tituloCampaniaInput.setText(it.nombre)
            descripcionCampaniaInput.setText(it.asunto)
            dominioCampaniaInput.setText(it.destinatarios)
            fechaInicioInput.setText(it.createdDate.toString())
            fechaFinInput.setText(it.scheduleDate.toString())
            contenidoInput.setText(it.contenido)
        }

        btnGuardar.setOnClickListener() {
            var idCampania = campania?.idCampania ?: 0
            val formCampania = loadCampania(idCampania)
            if (idCampania != 0) {
                actualizarCampania(formCampania)
            }else{
                guardarCampania(formCampania)
            }
        }

        btnCancelar.setOnClickListener() {
            finish()
        }
    }

    private fun showDatePickerDialog(editText: EditText, dateFormat: SimpleDateFormat) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                editText.setText(dateFormat.format(calendar.time))
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun loadCampania(camp: Int): CampaniaModel {
        val camp = CampaniaModel(
            tipo = tipoCampaniaInput.text.toString(),
            nombre = tituloCampaniaInput.text.toString(),
            asunto = descripcionCampaniaInput.text.toString(),
            destinatarios = dominioCampaniaInput.text.toString(),
            createdDate = fechaInicioInput.text.toString(),
            scheduleDate = fechaFinInput.text.toString(),
            contenido = contenidoInput.text.toString(),
            estatus = 1,
            idCampania = camp,
            idEmpresa = 1
        )

        Log.i("Campania cargada", camp.toString())

        return camp
    }

    private fun guardarCampania(campania: CampaniaModel) {
        RetrofitClient.instance.createCampania(campania).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CampaniaFormActivity, "Campania creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@CampaniaFormActivity, "Error en la respuesta: $error", Toast.LENGTH_SHORT).show()
                    Log.i("Error", error ?: "No se pudo obtener el mensaje de error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CampaniaFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.i("Error", call.toString())
            }
        })
    }

    private fun actualizarCampania(campania: CampaniaModel) {
        RetrofitClient.instance.updateCampania(campania.idCampania, campania).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CampaniaFormActivity, "Campania actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@CampaniaFormActivity, "Error en la respuesta: $error", Toast.LENGTH_SHORT).show()
                    Log.i("Error", error ?: "No se pudo obtener el mensaje de error")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CampaniaFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.i("Error", call.toString())
            }
        })
    }


}