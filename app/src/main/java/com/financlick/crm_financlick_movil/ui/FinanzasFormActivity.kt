package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.items.CardIngresosEgresosItem
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

class FinanzasFormActivity : AppCompatActivity() {
    private lateinit var idIngresoEgreso: TextInputEditText
    private lateinit var fecha: TextInputEditText
    private lateinit var monto: TextInputEditText
    private lateinit var descripcion: TextInputEditText
    private lateinit var tipo: TextInputEditText
    private lateinit var categoria: TextInputEditText

    private var contexto = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingreso_egreso_form)

        val ingresoEgresoRaw = intent.getStringExtra("ingresoEgreso").toString()
        val ingresoEgreso = Gson()  .fromJson(ingresoEgresoRaw, CardIngresosEgresosItem::class.java)


    }
}