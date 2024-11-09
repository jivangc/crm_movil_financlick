package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.config.SessionManager

class HomeActivity : AppCompatActivity() {
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        session = SessionManager(this)
        Log.i("SESION", session.getUser().toString())

        // ------- Navegacion ----------
        val quejasButton: ImageButton = findViewById(R.id.quejasButton)
        val empresasButton = findViewById<ImageButton>(R.id.empresasButton)
        val marketingButton = findViewById<ImageButton>(R.id.marketingButton)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val menuButton: ImageButton = findViewById(R.id.menuIcon)
        val ventasButton = findViewById<ImageButton>(R.id.btnVentas)
        val planificacionButton = findViewById<ImageButton>(R.id.btnPlanificacion)
        val finanzasButton = findViewById<ImageButton>(R.id.btnFinanzas)

        // Accesos Directos
        quejasButton.setOnClickListener{
            val intent = Intent(this, QuejasActivity::class.java)
            startActivity(intent)
        }
        empresasButton.setOnClickListener {
            val intent = Intent(this, EmpresasActivity::class.java)
            startActivity(intent)
        }
        marketingButton.setOnClickListener {
            val intent = Intent(this, MarketingActivity::class.java)
            startActivity(intent)
        }

        // Menu de Navegación
        menuButton.setOnClickListener {  // Set click listener on ImageButton
            drawerLayout.openDrawer(GravityCompat.START)
        }
        planificacionButton.setOnClickListener(){
            val intent = Intent(this, PlanificacionesPrincipal::class.java)
            startActivity(intent)
        }
        ventasButton.setOnClickListener(){
            val intent = Intent(this, VentasActivity::class.java)
            startActivity(intent)
        }
//        finanzasButton.setOnClickListener(){
//            val intent = Intent(this, FinanzasActivity::class.java)
//            startActivity(intent)
//        }
        // ------- Navegacion ----------
    }

}