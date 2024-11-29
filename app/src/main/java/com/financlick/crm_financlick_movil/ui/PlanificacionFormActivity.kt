package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.widget.LinearLayout

import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.PlanificacionModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlanificacionFormActivity : AppCompatActivity() {

    private lateinit var nombreInput: TextInputEditText
    private lateinit var apellidoInput: TextInputEditText
    private lateinit var telefonoInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var puestoInput: TextInputEditText
    private lateinit var btnGuardar: FloatingActionButton
    private lateinit var btnCancelar: FloatingActionButton
    private lateinit var btnLlamar: MaterialButton
    private var contacto: PlanificacionModel? = null
    private val REQUEST_CALL = 1
    private val REQUEST_CALL_PERMISSION = 1
    private var idEmpresa = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planificacion_form)

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        // Inicializar vistas
        nombreInput = findViewById(R.id.nombreInput)
        apellidoInput = findViewById(R.id.apellidoInput)
        telefonoInput = findViewById(R.id.telefonoInput)
        emailInput = findViewById(R.id.emailInput)
        puestoInput = findViewById(R.id.puestoInput)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnLlamar = findViewById(R.id.btnLlamar)

        idEmpresa = intent.getIntExtra("idEmpresa", 0)

        // Cargar contacto desde el Intent si existe
        val contactoRaw = intent.getStringExtra("contacto")
        contacto = if (!contactoRaw.isNullOrEmpty()) {
            Gson().fromJson(contactoRaw, PlanificacionModel::class.java).also {
                llenarCampos(it)
            }
        } else {
            null
        }


        // Configurar el botón de guardar
        btnGuardar.setOnClickListener {
            if (validateForm()) {
                val contactoRequest = cargarContactoRequest()
                if (contacto == null) {
                    guardarContacto(contactoRequest)
                } else {
                    actualizarContacto(contactoRequest)
                }
            }
        }

        // Configurar el botón de cancelar
        btnCancelar.setOnClickListener {
            val intent = Intent(this, PanificacionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Configurar el botón de llamar
        btnLlamar.setOnClickListener {
            val telefono = telefonoInput.text.toString()
            if (telefono.isNotEmpty()) {
                realizarLlamada(telefono)
            } else {
                Toast.makeText(this, "Por favor, ingrese un número de teléfono", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun callCards() {
        val intent = Intent(this, PanificacionActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    private fun llenarCampos(contacto: PlanificacionModel) {
        nombreInput.setText(contacto.nombre)
        apellidoInput.setText(contacto.apellido)
        telefonoInput.setText(contacto.telefono ?: "")
        emailInput.setText(contacto.email ?: "")
        puestoInput.setText(contacto.puesto ?: "")
    }

    private fun cargarContactoRequest(): PlanificacionModel {
        return PlanificacionModel(
            idContacto = contacto?.idContacto ?: 0,
            idEmpresa = contacto?.idEmpresa ?: 1,
            nombre = nombreInput.text.toString(),
            apellido = apellidoInput.text.toString(),
            telefono = telefonoInput.text.toString(),
            email = emailInput.text.toString(),
            puesto = puestoInput.text.toString()
        )
    }


    private fun validateForm(): Boolean {
        var isValid = true

        if (nombreInput.text.isNullOrBlank()) {
            nombreInput.error = "El nombre es requerido"
            isValid = false
        }
        if (apellidoInput.text.isNullOrBlank()) {
            apellidoInput.error = "El apellido es requerido"
            isValid = false
        }
        if (telefonoInput.text.isNullOrBlank() || telefonoInput.text.toString().length < 10) {
            telefonoInput.error = "El teléfono es requerido minimo 10 digitos"
            isValid = false
        }

        if (emailInput.text.isNullOrBlank()) {
            emailInput.error = "El email es requerido"
            isValid = false
        } else if (!isValidEmail(emailInput.text.toString())) {
            emailInput.error = "Por favor, ingrese un email válido"
            isValid = false
        }

        if(puestoInput.text.isNullOrBlank()) {
            puestoInput.error = "El puesto es requerido"
            isValid = false
        }

        return isValid
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }


    private fun guardarContacto(contacto: PlanificacionModel) {
        // Convertir ContactoModel a PlanificacionModel
        val planificacionModel = PlanificacionModel(
            // Asumiendo que PlanificacionModel tiene campos similares a ContactoModel
            // Ajusta estos valores según los campos reales de PlanificacionModel
            idContacto = contacto.idContacto,
            nombre = contacto.nombre,
            apellido = contacto.apellido,
            telefono = contacto.telefono,
            email = contacto.email,
            puesto = contacto.puesto,
            idEmpresa = idEmpresa
        )

        RetrofitClient.instance.createContacto(planificacionModel).enqueue(object : Callback<PlanificacionModel> {
            override fun onResponse(call: Call<PlanificacionModel>, response: Response<PlanificacionModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PlanificacionFormActivity, "Contacto guardado exitosamente", Toast.LENGTH_SHORT).show()
                    callCards()
                    //finish()
                } else {
                    Toast.makeText(this@PlanificacionFormActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlanificacionModel>, t: Throwable) {
                Toast.makeText(this@PlanificacionFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun actualizarContacto(contacto: PlanificacionModel) {
        // Convertir ContactoModel a PlanificacionModel
        val planificacionModel = PlanificacionModel(
            // Asumiendo que PlanificacionModel tiene campos similares a ContactoModel
            // Ajusta estos valores según los campos reales de PlanificacionModel
            idContacto = contacto.idContacto,
            nombre = contacto.nombre,
            apellido = contacto.apellido,
            telefono = contacto.telefono,
            email = contacto.email,
            puesto = contacto.puesto,
            idEmpresa = idEmpresa
        )

        RetrofitClient.instance.updateContacto(contacto.idContacto, planificacionModel)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PlanificacionFormActivity, "Contacto actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@PlanificacionFormActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@PlanificacionFormActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun realizarLlamada(numero: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL)
        } else {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$numero"))
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val telefono = telefonoInput.text.toString()
                realizarLlamada(telefono)
            } else {
                Toast.makeText(this, "Permiso para llamadas no concedido", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    
}