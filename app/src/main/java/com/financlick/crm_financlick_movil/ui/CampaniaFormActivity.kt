package com.financlick.crm_financlick_movil.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var btnGuardar: FloatingActionButton
    private lateinit var btnCancelar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_campania_form)

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        tipoCampaniaInput = findViewById(R.id.tipoCampaniaInput)
        tituloCampaniaInput = findViewById(R.id.tituloCampaniaInput)
        descripcionCampaniaInput = findViewById(R.id.descripcionCampaniaInput)
        dominioCampaniaInput = findViewById(R.id.dominioCampaniaInput)
        fechaInicioInput = findViewById(R.id.fechaInicioInput)
        fechaFinInput = findViewById(R.id.fechaFinInput)
        contenidoInput = findViewById(R.id.contenidoInput)

        btnGuardar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)
        val btnEliminar: FloatingActionButton = findViewById(R.id.btn_eliminar)

        val campaniaRaw = intent.getStringExtra("campania").toString()
        val campania = Gson().fromJson(campaniaRaw, CardCampaniaItem::class.java)

        campania?.let {
            // Llenar campos con datos de la campaña existente
            tipoCampaniaInput.setText(it.tipo)
            tituloCampaniaInput.setText(it.nombre)
            descripcionCampaniaInput.setText(it.asunto)
            dominioCampaniaInput.setText(it.destinatarios)
            fechaInicioInput.setText(it.createdDate.toString())
            fechaFinInput.setText(it.scheduleDate.toString())
            contenidoInput.setText(it.contenido)

            // Mostrar el botón de eliminar solo si se está editando
            btnEliminar.visibility = View.VISIBLE
            btnEliminar.setOnClickListener {
                eliminarCampania(campania.idCampania)
            }
        } ?: run {
            // Ocultar el botón de eliminar si se crea una nueva campaña
            btnEliminar.visibility = View.GONE
        }

        btnGuardar.setOnClickListener {
            if (validateFields()) {
                val idCampania = campania?.idCampania ?: 0
                val formCampania = loadCampania(idCampania)
                if (idCampania != 0) {
                    actualizarCampania(formCampania)
                } else {
                    guardarCampania(formCampania)
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun eliminarCampania(idCampania: Int) {
        RetrofitClient.instance.deleteCampania(idCampania).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CampaniaFormActivity, "Campaña eliminada exitosamente", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this@CampaniaFormActivity, CampanasActivity::class.java)
                    startActivity(intent)
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@CampaniaFormActivity, "Error al eliminar la campaña: $error", Toast.LENGTH_SHORT).show()
                    Log.e("EliminarCampania", "Error: $error")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CampaniaFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("EliminarCampania", "Error: ${t.message}")
            }
        })
    }



    private fun validateFields(): Boolean {
        var isValid = true

        if (tipoCampaniaInput.text.isNullOrBlank()) {
            tipoCampaniaInput.error = "El tipo de campaña es obligatorio"
            isValid = false
        }

        if (tituloCampaniaInput.text.isNullOrBlank()) {
            tituloCampaniaInput.error = "El título de la campaña es obligatorio"
            isValid = false
        }

        if (descripcionCampaniaInput.text.isNullOrBlank()) {
            descripcionCampaniaInput.error = "La descripción de la campaña es obligatoria"
            isValid = false
        }

        if (dominioCampaniaInput.text.isNullOrBlank()) {
            dominioCampaniaInput.error = "El dominio de la campaña es obligatorio"
            isValid = false
        }

        if (fechaInicioInput.text.isNullOrBlank()) {
            fechaInicioInput.error = "La fecha de inicio es obligatoria"
            isValid = false
        }

        if (fechaFinInput.text.isNullOrBlank()) {
            fechaFinInput.error = "La fecha de fin es obligatoria"
            isValid = false
        }

        if (contenidoInput.text.isNullOrBlank()) {
            contenidoInput.error = "El contenido es obligatorio"
            isValid = false
        }

        // Validar que la fecha de fin sea posterior a la fecha de inicio
        if (!fechaInicioInput.text.isNullOrBlank() && !fechaFinInput.text.isNullOrBlank()) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val fechaInicio = dateFormat.parse(fechaInicioInput.text.toString())
            val fechaFin = dateFormat.parse(fechaFinInput.text.toString())

            if (fechaFin != null && fechaInicio != null && fechaFin.before(fechaInicio)) {
                fechaFinInput.error = "La fecha de fin debe ser posterior a la fecha de inicio"
                isValid = false
            }
        }

        return isValid
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
                    var intent = Intent(this@CampaniaFormActivity, CampanasActivity::class.java)
                    startActivity(intent)
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
                    var intent = Intent(this@CampaniaFormActivity, CampanasActivity::class.java)
                    startActivity(intent)
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