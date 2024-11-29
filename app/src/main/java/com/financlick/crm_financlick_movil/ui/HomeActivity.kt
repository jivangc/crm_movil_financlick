package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.config.SessionManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Typeface
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.financlick.crm_financlick_movil.MainActivity
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuButton: ImageButton
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        session = SessionManager(this)


        // Helper Bottom Menu
        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        // ------- Inicialización de Drawer Layout y Menu Button ----------
        drawerLayout = findViewById(R.id.drawer_layout)
        menuButton = findViewById(R.id.menuIcon)
        navigationView = findViewById(R.id.nav_view)

        // ------- Navegación de botones en el DrawerMenu ----------
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // ------- Acciones del menú de navegación -----------
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Si ya estás en esta actividad, no hacer nada o puedes hacer un "toast"
                    Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    // Redirige a EmpresasActivity
                    startActivity(Intent(this, EmpresasActivity::class.java))
                }
                R.id.nav_plan -> {
                    // Redirige a PlanificacionesPrincipal
                    startActivity(Intent(this, PlanificacionesPrincipal::class.java))
                }
                R.id.nav_marketing -> {
                    // Redirige a MarketingActivity
                    startActivity(Intent(this, MarketingActivity::class.java))
                }
                R.id.nav_sales -> {
                    // Redirige a VentasActivity
                    startActivity(Intent(this, VentasActivity::class.java))
                }
                R.id.nav_finance -> {
                    // Redirige a FinanzasActivity
                    startActivity(Intent(this, FinanzasActivity::class.java))
                }
                R.id.nav_quejas -> {
                    // Redirige a QuejasActivity (si existe)
                    startActivity(Intent(this, QuejasActivity::class.java))
                }
                R.id.logout -> {
                    // Cerrar sesión
                    // Regresar al login o salir de la app
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Opcional si deseas finalizar la actividad actual
                }
                else -> {
                    // Acción por defecto
                    Toast.makeText(this, "Opción no definida", Toast.LENGTH_SHORT).show()
                }
            }
            // Cerrar el drawer después de que el usuario haya hecho una selección
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Configurar clics en las tarjetas
        val cardEmpresas: CardView = findViewById(R.id.cardEmpresas)
        val cardVentas: CardView = findViewById(R.id.cardVentas)
        val cardMarketing: CardView = findViewById(R.id.cardMarketing)
        val cardFinanzas: CardView = findViewById(R.id.cardFinanzas)
        val cardPlanificacion: CardView = findViewById(R.id.cardPlanificacion)

        cardEmpresas.setOnClickListener {
            startActivity(Intent(this, EmpresasActivity::class.java))
        }

        cardVentas.setOnClickListener {
            startActivity(Intent(this, VentasActivity::class.java))
        }

        cardMarketing.setOnClickListener {
            startActivity(Intent(this, MarketingActivity::class.java))
        }

        cardFinanzas.setOnClickListener {
            startActivity(Intent(this, FinanzasActivity::class.java))
        }

        cardPlanificacion.setOnClickListener {
            startActivity(Intent(this, PlanificacionesPrincipal::class.java))
        }
    }
}
