package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.financlick.crm_financlick_movil.R
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

class HomeActivity : AppCompatActivity() {
    lateinit var session: SessionManager
    private lateinit var barChart: BarChart


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
        barChart = findViewById(R.id.barChart)
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

        // Crear datos de ejemplo
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, 10f))
        entries.add(BarEntry(2f, 20f))
        entries.add(BarEntry(3f, 15f))
        entries.add(BarEntry(4f, 30f))
        entries.add(BarEntry(5f, 25f))

        // Crear el DataSet
        val dataSet = BarDataSet(entries, "Datos de ejemplo")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK

        // Crear el LineData con el DataSet
        val barData = BarData(dataSet)

        // Configurar el BarChart
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.setFitBars(true) // Ajusta las barras al gráfico
        barChart.animateY(1000) // Animación de entrada

        // Opciones adicionales de configuración
        barChart.axisRight.isEnabled = false // Desactiva el eje derecho
        barChart.xAxis.granularity = 1f // Intervalo en el eje X
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // Posición del eje X
        barChart.invalidate() // Refresca el gráfico
    }

}