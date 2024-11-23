package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R

class PlanificacionesPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_planificaciones_principal)
        val cardModule1 = findViewById<androidx.cardview.widget.CardView>(R.id.cardModule1)
        val cardModule2 = findViewById<androidx.cardview.widget.CardView>(R.id.cardModule2)

        cardModule1.setOnClickListener {
            val intent = Intent(this, PanificacionActivity::class.java)
            startActivity(intent)
        }

        cardModule2.setOnClickListener {
            val intent = Intent(this, ActividadActivity::class.java)
            startActivity(intent)
        }

    }
}