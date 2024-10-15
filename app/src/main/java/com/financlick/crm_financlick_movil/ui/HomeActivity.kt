package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val button = findViewById<ImageButton>(R.id.empresasButton)
        button.setOnClickListener {
            showEmpresas()
        }

    }
    fun showEmpresas() {
        val intent = Intent(this, EmpresasActivity::class.java)
        startActivity(intent)
    }
}