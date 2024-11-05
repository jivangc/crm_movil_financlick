package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.items.CardCampaniaItem
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

class CampaniaFormActivity : AppCompatActivity() {
    private lateinit var tipoCampaniaInput: TextInputEditText
    private lateinit var tituloCampaniaInput: TextInputEditText
    private lateinit var descripcionCampaniaInput: TextInputEditText
    private lateinit var dominioCampaniaInput: TextInputEditText
    private lateinit var fechaInicioInput: TextInputEditText
    private lateinit var fechaFinInput: TextInputEditText
    private lateinit var contenidoInput: TextInputEditText
    private lateinit var adjuntoInput: TextInputEditText
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
        adjuntoInput = findViewById(R.id.adjuntoInput)

        btnGuardar = findViewById(R.id.btnEnviar)
        btnCancelar = findViewById(R.id.btnCancelar)

        campania?.let {
            tipoCampaniaInput.setText(it.tipoCampania)
            tituloCampaniaInput.setText(it.titulo)
            descripcionCampaniaInput.setText(it.descripcion)
            dominioCampaniaInput.setText(it.dominioCampania)
            fechaInicioInput.setText(it.fechaInicio.toString())
            fechaFinInput.setText(it.fechaFin.toString())
            contenidoInput.setText(it.contenido)
            adjuntoInput.setText(it.adjunto)
        }

        btnGuardar.setOnClickListener() {
            Toast.makeText(this, "Botón guardar", Toast.LENGTH_SHORT).show()
        }

        btnCancelar.setOnClickListener() {
            finish()
        }
    }
}