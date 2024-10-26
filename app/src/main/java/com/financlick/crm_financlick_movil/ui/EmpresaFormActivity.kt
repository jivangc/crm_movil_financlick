package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardEmpresasItem
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class EmpresaFormActivity : AppCompatActivity() {
    private lateinit var nombreEmpresa: TextInputEditText
    private lateinit var razonSocial: TextInputEditText
    private lateinit var fechaConstitucion: TextInputEditText
    private lateinit var numeroEscritura: TextInputEditText
    private lateinit var nombreNotario: TextInputEditText
    private lateinit var numeroNotario: TextInputEditText
    private lateinit var folioMercantil: TextInputEditText
    private lateinit var rfc: TextInputEditText
    private lateinit var nombreRepresentanteLegal: TextInputEditText
    private lateinit var numeroEscrituraRepLeg: TextInputEditText
    private lateinit var fechaInscripcion: TextInputEditText
    private lateinit var calle: TextInputEditText
    private lateinit var colonia: TextInputEditText
    private lateinit var cp: TextInputEditText
    private lateinit var telefono: TextInputEditText
    private lateinit var estado: TextInputEditText
    private lateinit var localidad: TextInputEditText
    private lateinit var numExterior: TextInputEditText
    private lateinit var numInterior: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var estatus: TextInputEditText
    private lateinit var logo: TextInputEditText
    private lateinit var buttonGuardar: MaterialButton
    private lateinit var buttonCancelar: MaterialButton

    private var contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empresa_form)
        var empresaRaw = intent.getStringExtra("empresa").toString()
        val empresa = Gson().fromJson(empresaRaw, CardEmpresasItem::class.java)

        // Inicializar campos con los datos de la empresa

        nombreEmpresa = findViewById(R.id.et_nombreEmpresa)
        razonSocial = findViewById(R.id.et_razonSocial)
        fechaConstitucion = findViewById(R.id.et_fechaConstitucion)
        numeroEscritura = findViewById(R.id.et_numeroEscritura)
        nombreNotario = findViewById(R.id.et_nombreNotario)
        numeroNotario = findViewById(R.id.et_numeroNotario)
        folioMercantil = findViewById(R.id.et_folioMercantil)
        rfc = findViewById(R.id.et_rfc)
        nombreRepresentanteLegal = findViewById(R.id.et_nombreRepresentanteLegal)
        numeroEscrituraRepLeg = findViewById(R.id.et_numeroEscrituraRepLeg)
        fechaInscripcion = findViewById(R.id.et_fechaInscripcion)
        calle = findViewById(R.id.et_calle)
        colonia = findViewById(R.id.et_colonia)
        cp = findViewById(R.id.et_cp)
        telefono = findViewById(R.id.et_telefono)
        estado = findViewById(R.id.et_estado)
        localidad = findViewById(R.id.et_localidad)
        numExterior = findViewById(R.id.et_numExterior)
        numInterior = findViewById(R.id.et_numInterior)
        email = findViewById(R.id.et_email)
        //estatus = findViewById(R.id.)
        logo = findViewById(R.id.et_logo)

        buttonGuardar = findViewById(R.id.btn_submit)
        buttonCancelar = findViewById(R.id.btn_cancel)


        //val etFechaInscripcion = findViewById<TextInputEditText>(R.id.et_fechaInscripcion)

        fechaInscripcion.addTextChangedListener(object : TextWatcher {
            private var isUpdating: Boolean = false
            private val dateFormat = "dd/MM/yyyy"
            private val dateRegex = Regex("\\d{2}/\\d{2}/\\d{4}")

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                val input = s.toString().replace(Regex("[^\\d]"), "")
                val formatted = when {
                    input.length <= 2 -> input
                    input.length <= 4 -> "${input.substring(0, 2)}/${input.substring(2)}"
                    input.length <= 8 -> "${input.substring(0, 2)}/${input.substring(2, 4)}/${input.substring(4)}"
                    else -> input
                }

                isUpdating = true
                fechaInscripcion.setText(formatted)
                fechaInscripcion.setSelection(formatted.length)
            }

            override fun afterTextChanged(s: Editable?) {
                // Opcionalmente, puedes validar el formato completo aquí
                s?.let {
                    if (!it.matches(dateRegex)) {
                        fechaInscripcion.error = "Formato de fecha inválido (dd/MM/yyyy)"
                    }
                }
            }
        })


        fechaConstitucion.addTextChangedListener(object : TextWatcher {
            private var isUpdating: Boolean = false
            private val dateFormat = "dd/MM/yyyy"
            private val dateRegex = Regex("\\d{2}/\\d{2}/\\d{4}")

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                val input = s.toString().replace(Regex("[^\\d]"), "")
                val formatted = when {
                    input.length <= 2 -> input
                    input.length <= 4 -> "${input.substring(0, 2)}/${input.substring(2)}"
                    input.length <= 8 -> "${input.substring(0, 2)}/${input.substring(2, 4)}/${input.substring(4)}"
                    else -> input
                }

                isUpdating = true
                fechaConstitucion.setText(formatted)
                fechaConstitucion.setSelection(formatted.length)
            }

            override fun afterTextChanged(s: Editable?) {
                // Opcionalmente, puedes validar el formato completo aquí
                s?.let {
                    if (!it.matches(dateRegex)) {
                        fechaConstitucion.error = "Formato de fecha inválido (dd/MM/yyyy)"
                    }
                }
            }
        })


        empresa?.let {
            nombreEmpresa.setText(it.nombreEmpresa)
            razonSocial.setText(it.razonSocial)
            fechaConstitucion.setText(it.fechaConstitucion)
            numeroEscritura.setText(it.numeroEscritura)
            nombreNotario.setText(it.nombreNotario)
            numeroNotario.setText(it.numeroNotario)
            folioMercantil.setText(it.folioMercantil)
            rfc.setText(it.rfc)
            nombreRepresentanteLegal.setText(it.nombreRepresentanteLegal)
            numeroEscrituraRepLeg.setText(it.numeroEscrituraRepLeg)
            fechaInscripcion.setText(it.fechaInscripcion)
            calle.setText(it.calle)
            colonia.setText(it.colonia)
            cp.setText(it.cp)
            telefono.setText(it.telefono)
            estado.setText(it.estado)
            localidad.setText(it.localidad)
            numExterior.setText(it.numExterior)
            numInterior.setText(it.numInterior)
            email.setText(it.email)
            //estatus.setText(it.estatus)
            logo.setText(it.logo)
        }

        buttonGuardar.setOnClickListener(){
            val request = loadEmpresaRequest()
//            if (empresa.idEmpresa == 0) {
//                //Guardar nueva empresa
//                Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show()
//                guardarEmpresa(request)
//            }else {
//                //ActualizarEmpresa
//                Toast.makeText(this, "Actualizando", Toast.LENGTH_SHORT).show()
//                actualizarEmpresa(request)
//            }
            //Guardar nueva empresa
            Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show()
            guardarEmpresa(request)
        }

        buttonCancelar.setOnClickListener(){
            finish()
        }
    }



    private fun loadEmpresaRequest(): EmpresaModel {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val fechaConstitucionDate = try {
            dateFormat.parse(fechaConstitucion.text.toString())
        } catch (e: Exception) {
            null // Maneja el caso en que la fecha no sea válida
        }

        val fechaInscripcionDate = try {
            dateFormat.parse(fechaInscripcion.text.toString())
        } catch (e: Exception) {
            null // Maneja el caso en que la fecha no sea válida
        }

        val empresa = EmpresaModel(
            idEmpresa = 0,
            nombreEmpresa = nombreEmpresa.text.toString(),
            razonSocial = razonSocial.text.toString(),
            fechaConstitucion = fechaConstitucionDate,
            numeroEscritura = numeroEscritura.text.toString(),
            nombreNotario = nombreNotario.text.toString(),
            numeroNotario = numeroNotario.text.toString(),
            folioMercantil = folioMercantil.text.toString(),
            rfc = rfc.text.toString(),
            nombreRepresentanteLegal = nombreRepresentanteLegal.text.toString(),
            numeroEscrituraRepLeg = numeroEscrituraRepLeg.text.toString(),
            fechaInscripcion = fechaInscripcionDate,
            calle = calle.text.toString(),
            colonia = colonia.text.toString(),
            cp = cp.text.toString(),
            telefono = telefono.text.toString(),
            estado = estado.text.toString(),
            localidad = localidad.text.toString(),
            numExterior = numExterior.text.toString(),
            numInterior = numInterior.text.toString(),
            email = email.text.toString(),
            estatus = 1,
            logo = logo.text.toString()
        )

        return empresa
    }



    private fun guardarEmpresa(param: EmpresaModel) {
        RetrofitClient.instance.createEmpresa(param).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(contexto, "Empresa guardada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(contexto, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                    Log.e("Error", "Código: ${response.code()}")
                    Log.e("Error", "Cuerpo: $errorBody")
                    Log.e("Error", "Mensaje: ${response.message()}")
                    Log.e("Error", "Raw: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

//    private fun actualizarEmpresa(param: idEmpresa) {
//        RetrofitClient.instance.updateEmpresa(param).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(contexto, "Empresa guardada exitosamente", Toast.LENGTH_SHORT).show()
//                    finish()
//                } else {
//                    val errorBody = response.errorBody()?.string()
//                    Toast.makeText(contexto, "Error: $errorBody", Toast.LENGTH_SHORT).show()
//                    Log.e("Error", "Código: ${response.code()}")
//                    Log.e("Error", "Cuerpo: $errorBody")
//                    Log.e("Error", "Mensaje: ${response.message()}")
//                    Log.e("Error", "Raw: ${response.raw()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}