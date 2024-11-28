package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardEmpresasItem
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.EmpresaModelSave
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class EmpresaFormActivity : AppCompatActivity() {
    private lateinit var idEmpresa: TextInputEditText
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
    private lateinit var buttonGuardar: FloatingActionButton
    private lateinit var buttonCancelar: FloatingActionButton
    private lateinit var buttonEliminar: FloatingActionButton

    private var contexto = this

    private lateinit var btnSelectImage: MaterialButton
    private lateinit var ivLogoPreview: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var logo_b64: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empresa_form)
        var empresaRaw = intent.getStringExtra("empresa").toString()
        val empresa = Gson().fromJson(empresaRaw, CardEmpresasItem::class.java)

        // Inicializar campos con los datos de la empresa
        idEmpresa = findViewById(R.id.et_idEmpresa)
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
        // logo = findViewById(R.id.et_logo)

        //Seccion de botones
        buttonGuardar = findViewById(R.id.btn_submit)
        buttonCancelar = findViewById(R.id.btn_cancel)
        buttonEliminar = findViewById(R.id.btn_eliminar)

        if (empresa != null && empresa.idEmpresa != 0) {
            // Si hay una empresa seleccionada y tiene un id distinto de 0, muestra el botón de eliminar
            buttonEliminar.visibility = View.VISIBLE
        } else {
            // Si se está agregando una nueva empresa, mantén el botón oculto
            buttonEliminar.visibility = View.GONE
        }

        btnSelectImage = findViewById(R.id.btn_select_image)
        ivLogoPreview = findViewById(R.id.iv_logo_preview)

        btnSelectImage.setOnClickListener {
            openImageChooser()
        }

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
            idEmpresa.setText(it.idEmpresa.toString())
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
            logo_b64 = it.logo
        }

        buttonGuardar.setOnClickListener {
            if (validateFields()) {
                val request = loadEmpresaRequest()
                if (request.idEmpresa == 0) {
                    // Guardar nueva empresa
                    Toast.makeText(this, "Guardando", Toast.LENGTH_SHORT).show()
                    guardarEmpresa(request)
                } else {
                    // Actualizar empresa
                    Toast.makeText(this, "Actualizando", Toast.LENGTH_SHORT).show()
                    actualizarEmpresa(request)
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }


        buttonCancelar.setOnClickListener(){
            finish()
        }

        buttonEliminar.setOnClickListener(){
            showDeleteConfirmationDialog()
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            ivLogoPreview.setImageURI(imageUri)

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val base64Image = bitmapToBase64(bitmap)
            logo_b64 = base64Image
            //logo.setText(base64Image)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    //Funcion para validar los campos
    private fun validateFields(): Boolean {
        var isValid = true

        if (nombreEmpresa.text.isNullOrBlank()) {
            nombreEmpresa.error = "El nombre de la empresa es obligatorio"
            isValid = false
        }

        if (razonSocial.text.isNullOrBlank()) {
            razonSocial.error = "La razón social es obligatoria"
            isValid = false
        }

        if (fechaConstitucion.text.isNullOrBlank()) {
            fechaConstitucion.error = "La fecha de constitución es obligatoria"
            isValid = false
        }

        if (numeroEscritura.text.isNullOrBlank()) {
            numeroEscritura.error = "El número de escritura es obligatorio"
            isValid = false
        }

        if (nombreNotario.text.isNullOrBlank()) {
            nombreNotario.error = "El nombre del notario es obligatorio"
            isValid = false
        }

        if (numeroNotario.text.isNullOrBlank()) {
            numeroNotario.error = "El número de notario es obligatorio"
            isValid = false
        }

        if (folioMercantil.text.isNullOrBlank()) {
            folioMercantil.error = "El folio mercantil es obligatorio"
            isValid = false
        }

        if (rfc.text.isNullOrBlank() || rfc.text.toString().length > 13 || rfc.text.toString().length < 12) {
            rfc.error = "El RFC es obligatorio"
            isValid = false
        }

        if (nombreRepresentanteLegal.text.isNullOrBlank()) {
            nombreRepresentanteLegal.error = "El nombre del representante legal es obligatorio"
            isValid = false
        }

        if (numeroEscrituraRepLeg.text.isNullOrBlank()) {
            numeroEscrituraRepLeg.error = "El número de escritura del representante legal es obligatorio"
            isValid = false
        }

        if (fechaInscripcion.text.isNullOrBlank()) {
            fechaInscripcion.error = "La fecha de inscripción es obligatoria"
            isValid = false
        }

        if (calle.text.isNullOrBlank()) {
            calle.error = "La calle es obligatoria"
            isValid = false
        }

        if (colonia.text.isNullOrBlank()) {
            colonia.error = "La colonia es obligatoria"
            isValid = false
        }

        if (cp.text.isNullOrBlank() || cp.text.toString().length != 5) {
            cp.error = "El código postal es obligatorio"
            isValid = false
        }

        if (telefono.text.isNullOrBlank() || telefono.text.toString().length < 10) {
            telefono.error = "El teléfono es obligatorio minimo 10 digitos"
            isValid = false
        }

        if (estado.text.isNullOrBlank()) {
            estado.error = "El estado es obligatorio"
            isValid = false
        }

        if (localidad.text.isNullOrBlank()) {
            localidad.error = "La localidad es obligatoria"
            isValid = false
        }

        if (numExterior.text.isNullOrBlank()) {
            numExterior.error = "El número exterior es obligatorio"
            isValid = false
        }

        if (numInterior.text.isNullOrBlank()) {
            numInterior.error = "El número interior es obligatorio"
            isValid = false
        }

        if (email.text.isNullOrBlank()) {
            email.error = "El correo electrónico es obligatorio"
            isValid = false
        }else if (!isValidEmail(email.text.toString())) {
            email.error = "Por favor, ingrese un email válido"
            isValid = false
        }

        return isValid
    }



    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
    private fun loadEmpresaRequest(): EmpresaModelSave {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val formattedFechaConstitucion = fechaConstitucion.text.toString().let {
            try { formatDateForApi(it) } catch (e: Exception) { null }
        }

        val formattedFechaInscripcion = fechaInscripcion.text.toString().let {
            try { formatDateForApi(it) } catch (e: Exception) { null }
        }


        val empresa = EmpresaModelSave(
            idEmpresa = idEmpresa.text.toString().toIntOrNull() ?: 0,
            nombreEmpresa = nombreEmpresa.text.toString(),
            razonSocial = razonSocial.text.toString(),
            fechaConstitucion = formattedFechaConstitucion,
            numeroEscritura = numeroEscritura.text.toString(),
            nombreNotario = nombreNotario.text.toString(),
            numeroNotario = numeroNotario.text.toString(),
            folioMercantil = folioMercantil.text.toString(),
            rfc = rfc.text.toString(),
            nombreRepresentanteLegal = nombreRepresentanteLegal.text.toString(),
            numeroEscrituraRepLeg = numeroEscrituraRepLeg.text.toString(),
            fechaInscripcion = formattedFechaInscripcion,
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
            logo = logo_b64
        )



        return empresa
    }




    private fun guardarEmpresa(param: EmpresaModelSave) {
        RetrofitClient.instance.createEmpresa(param).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(contexto, "Empresa guardada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(contexto, EmpresasActivity::class.java)
                    startActivity(intent)
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

    private fun actualizarEmpresa(param: EmpresaModelSave) {
        RetrofitClient.instance.updateEmpresa(param, param.idEmpresa).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(contexto, "Empresa actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(contexto, EmpresasActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(contexto, "Error: $errorBody", Toast.LENGTH_SHORT).show()
                    Log.e("Error", "Código: ${response.code()}")
                    Log.e("Error", "Cuerpo: $errorBody")
                    Log.e("Error", "Detalle: ${response.message()}")
                    Log.e("Error", "Raw: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarEmpresa() {
        RetrofitClient.instance.deleteEmpresa(idEmpresa.text.toString().toInt()).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(contexto, "Empresa eliminada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(contexto, EmpresasActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(contexto, "Error al eliminar la empresa", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(contexto, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // Función para mostrar el diálogo de confirmación
    private fun showDeleteConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar esta empresa?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                // Llama a la función para eliminar la empresa aquí
                eliminarEmpresa()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                // Cierra el diálogo sin realizar ninguna acción
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }


    private val localFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val apiFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatDateForApi(localDate: String): String {
        return try {
            val date = localFormat.parse(localDate)
            apiFormat.format(date)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid date format. Use DD/MM/YYYY")
        }
    }

    fun formatDateFromApi(apiDate: String): String {
        return try {
            val date = apiFormat.parse(apiDate)
            localFormat.format(date)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid API date format")
        }
    }
}