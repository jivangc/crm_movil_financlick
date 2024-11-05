package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R

class MarketingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_marketing)

        val cardModule1 = findViewById<androidx.cardview.widget.CardView>(R.id.cardModule1)
        val cardModule2 = findViewById<androidx.cardview.widget.CardView>(R.id.cardModule2)
        val cardModule3 = findViewById<androidx.cardview.widget.CardView>(R.id.cardModule3)

        cardModule1.setOnClickListener {
            val intent = Intent(this, CampanasActivity::class.java)
            startActivity(intent)
            finish()
        }

        cardModule2.setOnClickListener {
            val intent = Intent(this, SolicitudesCampaniasActivity::class.java)
            startActivity(intent)
            finish()
        }

        cardModule3.setOnClickListener {
            val intent = Intent(this, SitioWebActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}