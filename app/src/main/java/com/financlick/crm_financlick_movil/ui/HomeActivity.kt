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
        setContentView(R.layout.activity_home)
        enableEdgeToEdge()

        val button = findViewById<ImageButton>(R.id.empresasButton)
        val quejasButton: ImageButton = findViewById(R.id.quejasButton)

        quejasButton.setOnClickListener{
            val intent = Intent(this, QuejasActivity::class.java)
            startActivity(intent)
        }

    }


}