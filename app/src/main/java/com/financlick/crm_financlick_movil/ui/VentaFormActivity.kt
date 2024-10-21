package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.financlick.crm_financlick_movil.models.VentasModel
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
    private lateinit var btnEnviar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private var contexto = this

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_venta_form)
        var ventaRaw = intent.getStringExtra("venta").toString()
        val venta = Gson().fromJson(ventaRaw, CardVentaItem::class.java)

        //Inicializar vistas

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

        btnEnviar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)

        venta?.let {
            planInput.setText(it.idPlan)
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

        btnEnviar.setOnClickListener(){
            val request = loadVentaRequest()
            if(venta.idVenta == 0){
                //Guardar nuevo
                Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show()
                guardarVenta(request)
            }else{
                // Actualizar
                Toast.makeText(this, "Actualizacion", Toast.LENGTH_SHORT).show()
                actualizarVenta(request)
            }
        }

        btnCancelar.setOnClickListener(){
            finish()
        }
    }

    private fun loadVentaRequest(): VentasModel {
        val venta = VentasModel(
            idVenta = 0,
            idVPlan = planInput.text.toString().toIntOrNull() ?: 0,
            fechaSolicitud = fechaSolicitud.text.toString(),
            nombreCliente = nombreCliente.text.toString(),
            nombreEmpresa = nombreEmpresa.text.toString(),
            numeroContacto = numeroContacto.text.toString(),
            correo = correo.text.toString(),
            domicilio = domicilio.text.toString(),
            ciudad = ciudad.text.toString(),
            estado = estado.text.toString(),
            rfc = rfc.text.toString()
        )
        return venta
    }

    private fun guardarVenta(param: VentasModel){
        RetrofitClient.instance.createVenta(param).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if(response.isSuccessful){
                    Toast.makeText(contexto, "Venta añadida", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(contexto, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarVenta(param: VentasModel) {

    }
}