package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.models.UsuarioModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VentaFormActivity : AppCompatActivity() {

    private lateinit var planInput: TextInputEditText
    private lateinit var fechaSolicitud: TextInputEditText
    private lateinit var nombreCliente: TextInputEditText
    private lateinit var nombreEmpresa: TextInputEditText
    private lateinit var numeroContacto: TextInputEditText
    private lateinit var correo: TextInputEditText
    private lateinit var domicilio: TextInputEditText
    private lateinit var ciudad: TextInputEditText
    private lateinit var estado: TextInputEditText
    private lateinit var rfc: TextInputEditText
    private lateinit var empleadoSpinner: Spinner
    private lateinit var btnEnviar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private var ventaActual: CardVentaItem? = null
    private var empleados = listOf<UsuarioModel>()
    private var contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_venta_form)

        inicializarVistas()

        // Obtener la venta actual utilizando la clave correcta
        ventaActual = intent.getSerializableExtra("VENTA_ACTUAL") as? CardVentaItem

        mostrarDatosVenta()

        fetchUsuariosPorRol()

        btnEnviar.setOnClickListener {
            //val request = mapOf("idUsuarioEncargado" to (empleadoSpinner.selectedItemPosition - 1))
            actualizarVenta()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun inicializarVistas() {
        planInput = findViewById(R.id.planInput)
        fechaSolicitud = findViewById(R.id.fechaSolicitudInput)
        nombreCliente = findViewById(R.id.nombreClienteInput)
        nombreEmpresa = findViewById(R.id.nombreEmpresaInput)
        numeroContacto = findViewById(R.id.numeroContactoInput)
        correo = findViewById(R.id.correoInput)
        domicilio = findViewById(R.id.domicilioInput)
        ciudad = findViewById(R.id.ciudadInput)
        estado = findViewById(R.id.estadoInput)
        rfc = findViewById(R.id.rfcInput)
        empleadoSpinner = findViewById(R.id.empleadoSpinner)
        btnEnviar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)
    }

    private fun mostrarDatosVenta() {
        ventaActual?.let {
            planInput.setText(it.idVPlan.toString())
            fechaSolicitud.setText(it.fechaSolicitud)
            nombreCliente.setText(it.nombreCliente)
            nombreEmpresa.setText(it.nombreEmpresa)
            numeroContacto.setText(it.numeroContacto)
            correo.setText(it.correo)
            domicilio.setText(it.domicilio)
            ciudad.setText(it.ciudad)
            estado.setText(it.estado)
            rfc.setText(it.rfc)
        }
    }

    private fun fetchUsuariosPorRol() {
        RetrofitClient.instance.getUsuariosPorRol().enqueue(object : Callback<List<UsuarioModel>> {
            override fun onResponse(call: Call<List<UsuarioModel>>, response: Response<List<UsuarioModel>>) {
                if (response.isSuccessful) {
                    empleados = response.body() ?: emptyList()

                    val employeeNames = mutableListOf("Selecciona un empleado")
                    employeeNames.addAll(empleados.map { "${it.nombre} ${it.apellidoPaterno}" })

                    val adapter = ArrayAdapter(this@VentaFormActivity, android.R.layout.simple_spinner_item, employeeNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    empleadoSpinner.adapter = adapter

                    if (ventaActual?.idUsuario != null) {
                        val position = empleados.indexOfFirst { it.idUsuario == ventaActual?.idUsuario }
                        if (position != -1) {
                            empleadoSpinner.setSelection(position + 1)
                        }
                    }
                } else {
                    Toast.makeText(contexto, "Error al cargar empleados: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UsuarioModel>>, t: Throwable) {
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarVenta() {
        // Obtenemos el idUsuario desde el spinner de empleados
        val idUsuario = empleados.getOrNull(empleadoSpinner.selectedItemPosition - 1)?.idUsuario
        Log.d("VentaFormActivity", "ID de usuario seleccionado: $idUsuario")
        if (idUsuario == null) {
            Toast.makeText(contexto, "Seleccione un empleado válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Creamos el map con los datos a enviar y lo registramos en el log
        val fields = mapOf("idUsuario" to idUsuario)
        Log.d("VentaFormActivity", "Datos a enviar al backend: $fields")

        // Obtenemos el idVenta de la venta actual y verificamos que no sea nulo
        val idVenta = ventaActual?.idVenta ?: return
        Log.d("VentaFormActivity", "ID de venta seleccionada: $idVenta")

        // Llamada a la API
        RetrofitClient.instance.updateVenta(idVenta, fields).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // Log de la respuesta completa para ver todos los detalles
                Log.d("VentaFormActivity", "Respuesta recibida: ${response.raw()}")

                if (response.isSuccessful) {
                    Log.d("VentaFormActivity", "Actualización exitosa en el backend")
                    Toast.makeText(contexto, "Venta actualizada", Toast.LENGTH_SHORT).show()

                    // Ir a HomeActivity y finalizar la actividad actual
                    val intent = Intent(this@VentaFormActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish() // Cierra VentaFormActivity
                } else {
                    // Detalles de error cuando no es exitoso
                    Log.e("VentaFormActivity", "Error en la respuesta: código ${response.code()}, mensaje: ${response.message()}, cuerpo: ${response.errorBody()?.string()}")
                    Toast.makeText(contexto, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Log de la excepción en caso de falla
                Log.e("VentaFormActivity", "Error en la solicitud: ${t.message}")
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
