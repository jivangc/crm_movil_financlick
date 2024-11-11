package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.config.SessionManager
import com.financlick.crm_financlick_movil.models.EmpresaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    private lateinit var txtAprobado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        session = SessionManager(this)
        Log.i("SESION", session.getUser().toString())

        txtAprobado = findViewById(R.id.txtAprobado)

        obtenerEmpresas()

        // ------- Navegacion ----------
        val quejasButton: ImageButton = findViewById(R.id.quejasButton)
        val empresasButton: ImageButton = findViewById(R.id.empresasButton)
        val marketingButton: ImageButton = findViewById(R.id.marketingButton)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val menuButton: ImageButton = findViewById(R.id.menuIcon)
        val ventasButton: ImageButton = findViewById(R.id.btnVentas)
        val planificacionButton: ImageButton = findViewById(R.id.btnPlanificacion)
        val finanzasButton: ImageButton = findViewById(R.id.btnFinanzas)

        // Accesos Directos
        quejasButton.setOnClickListener {
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
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        planificacionButton.setOnClickListener {
            val intent = Intent(this, PanificacionActivity::class.java)
            startActivity(intent)
        }
        ventasButton.setOnClickListener {
            val intent = Intent(this, VentasActivity::class.java)
            startActivity(intent)
        }
        // finanzasButton.setOnClickListener {
        //     val intent = Intent(this, FinanzasActivity::class.java)
        //     startActivity(intent)
        // }
        // ------- Navegacion ----------
    }

    private fun obtenerEmpresas() {
        RetrofitClient.instance.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                if (response.isSuccessful) {
                    val empresas = response.body() ?: emptyList()
                    // Aquí puedes contar las empresas y actualizar txtAprobado
                    val conteoEmpresas = empresas.count()
                    txtAprobado.text = "$conteoEmpresas Empresas"
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener las empresas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
