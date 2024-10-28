package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.config.SessionManager

class HomeActivity : AppCompatActivity() {
    lateinit var session: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        session = SessionManager(this)
        val quejasButton: ImageButton = findViewById(R.id.quejasButton)
        val button = findViewById<ImageButton>(R.id.empresasButton)
        val ventasButton = findViewById<ImageButton>(R.id.perfilButton)

        Log.i("SESION", session.getUser().toString())

        ventasButton.setOnClickListener(){
            val intent = Intent(this, VentasActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            showEmpresas()
        }

        quejasButton.setOnClickListener{
            val intent = Intent(this, QuejasActivity::class.java)
            startActivity(intent)
        }
    }

    fun showEmpresas() {
        val intent = Intent(this, EmpresasActivity::class.java)
        startActivity(intent)
    }
}