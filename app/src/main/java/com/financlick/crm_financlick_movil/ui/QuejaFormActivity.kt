package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.financlick.crm_financlick_movil.models.QuejaRequestModel
import com.financlick.crm_financlick_movil.models.UsuarioModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var estatusSpinner: Spinner
    private lateinit var empleadoSpinner: Spinner
    private lateinit var btnEnviar: FloatingActionButton
    private lateinit var btnEliminar: FloatingActionButton
    private lateinit var btnCancelar: FloatingActionButton
    private var empleados = listOf<UsuarioModel>()
    private var queja: CardQuejaItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_queja_form)

        // Inicializar vistas
        tipoInput = findViewById(R.id.tipoInput)
        descripcionInput = findViewById(R.id.descripcionInput)
        prioridadSpinner = findViewById(R.id.prioridadSpinner)
        estatusSpinner = findViewById(R.id.estatusSpinner)
        empleadoSpinner = findViewById(R.id.empleadoSpinner)
        btnEnviar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnEliminar = findViewById(R.id.btnEliminar)

        // Deshabilitar campos no editables
        tipoInput.isEnabled = false
        descripcionInput.isEnabled = false

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        // Configurar Spinners
        setupPrioritySpinner()
        setupStatusSpinner()
        fetchUsuariosPorRol()

        // Cargar datos iniciales
        val quejaRaw = intent.getStringExtra("queja").toString()
        queja = Gson().fromJson(quejaRaw, CardQuejaItem::class.java)
        populateForm(queja)

        if (tipoInput.text.toString() == "Q") {
            tipoInput.setText("Queja")
        } else {
            tipoInput.setText("Sugerencia")
        }

        // Botón enviar
        btnEnviar.setOnClickListener {
            val request = loadQuejaRequest()
            if (queja != null && queja!!.idQuejaSugerencia > 0) {
                val quejaModel = toQuejaModel(request)
                actualizarQueja(quejaModel, queja!!.idQuejaSugerencia)
            } else {
                guardarQueja(request)
            }
        }

        // Botón cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Botón eliminar
        btnEliminar.setOnClickListener {
            queja?.idQuejaSugerencia?.let { id ->
                eliminarQueja(id)
            } ?: Toast.makeText(this, "No se puede eliminar una queja inexistente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun eliminarQueja(idQueja: Int) {
        RetrofitClient.instance.deleteQueja(idQueja).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuejaFormActivity, "Queja eliminada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@QuejaFormActivity, "Error al eliminar la queja: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@QuejaFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupPrioritySpinner() {
        val prioridades = listOf("Baja", "Media", "Alta")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioridadSpinner.adapter = adapter
    }

    private fun setupStatusSpinner() {
        val estatus = listOf("Pendiente", "Revisión", "Resuelto", "Cancelado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estatus)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        estatusSpinner.adapter = adapter
    }

    private fun fetchUsuariosPorRol() {
        RetrofitClient.instance.getUsuariosPorRol().enqueue(object : Callback<List<UsuarioModel>> {
            override fun onResponse(call: Call<List<UsuarioModel>>, response: Response<List<UsuarioModel>>) {
                if (response.isSuccessful) {
                    empleados = response.body() ?: emptyList()
                    val nombres = listOf("Selecciona un empleado") + empleados.map { "${it.nombre} ${it.apellidoPaterno}" }
                    val adapter = ArrayAdapter(this@QuejaFormActivity, android.R.layout.simple_spinner_item, nombres)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    empleadoSpinner.adapter = adapter

                    // Preseleccionar responsable si aplica
                    queja?.responsable?.let { idResponsable ->
                        val position = empleados.indexOfFirst { it.idUsuario == idResponsable }
                        if (position >= 0) empleadoSpinner.setSelection(position + 1)
                    }
                }
            }

            override fun onFailure(call: Call<List<UsuarioModel>>, t: Throwable) {
                Toast.makeText(this@QuejaFormActivity, "Error al cargar empleados: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateForm(queja: CardQuejaItem?) {
        queja?.let {
            tipoInput.setText(it.tipo)
            descripcionInput.setText(it.descripcion)
            prioridadSpinner.setSelection(it.prioridad?.minus(1) ?: 0)
            estatusSpinner.setSelection(it.estatus?.minus(1) ?: 0)
        }
    }

    private fun loadQuejaRequest(): QuejaRequestModel {
        val tipo = if (tipoInput.text.toString() == "Queja") "Q" else "S"
        val responsableSeleccionado = empleadoSpinner.selectedItemPosition - 1
        return QuejaRequestModel(
            idQuejaSugerencia = queja?.idQuejaSugerencia ?: 0,
            idEmpresa = queja?.idEmpresa ?: 0,
            tipo = tipo,
            descripcion = descripcionInput.text.toString(),
            fechaRegistro = queja?.fechaRegistro ?: "",
            estado = estatusSpinner.selectedItemPosition + 1,
            fechaResolucion = null,
            responsable = if (responsableSeleccionado >= 0) empleados[responsableSeleccionado].idUsuario else null,
            prioridad = prioridadSpinner.selectedItemPosition + 1,
            comentarios = null
        )
    }

    private fun guardarQueja(param: QuejaRequestModel) {
        RetrofitClient.instance.createQueja(param).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuejaFormActivity, "Queja creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@QuejaFormActivity, "Error al crear la queja: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@QuejaFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarQueja(param: QuejaModel, idQueja: Int) {
        RetrofitClient.instance.updateQueja(idQueja, param).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuejaFormActivity, "Queja actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    Toast.makeText(this@QuejaFormActivity, "Error al actualizar la queja: $error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@QuejaFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toQuejaModel(request: QuejaRequestModel): QuejaModel {
        return QuejaModel(
            idQuejaSugerencia = request.idQuejaSugerencia,
            idEmpresa = request.idEmpresa,
            tipo = request.tipo,
            descripcion = request.descripcion,
            fechaRegistro = request.fechaRegistro,
            estado = request.estado,
            fechaResolucion = request.fechaResolucion,
            responsable = request.responsable,
            prioridad = request.prioridad,
            comentarios = request.comentarios
        )
    }


}
