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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.config.SessionManager
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Color
import android.graphics.Typeface


class HomeActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    private lateinit var txtAprobado: TextView
    private lateinit var tablaSolicitudes: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        session = SessionManager(this)
        Log.i("SESION", session.getUser().toString())

        txtAprobado = findViewById(R.id.txtAprobado)
        tablaSolicitudes = findViewById(R.id.tablaSolicitudes)

        obtenerEmpresas()
        obtenerIngresos()

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
    }

    private fun obtenerEmpresas() {
        RetrofitClient.instance.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                if (response.isSuccessful) {
                    val empresas = response.body() ?: emptyList()
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

    private fun obtenerIngresos() {
        RetrofitClient.instance.getIngresos().enqueue(object : Callback<List<IngresosEgresoModel>> {
            override fun onResponse(call: Call<List<IngresosEgresoModel>>, response: Response<List<IngresosEgresoModel>>) {
                if (response.isSuccessful) {
                    val ingresos = response.body() ?: emptyList()
                    mostrarIngresosEnTabla(ingresos)
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener los ingresos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<IngresosEgresoModel>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarIngresosEnTabla(ingresos: List<IngresosEgresoModel>) {
        val tablaSolicitudes: TableLayout = findViewById(R.id.tablaSolicitudes)
        tablaSolicitudes.removeAllViews()

        // Agrega el encabezado de la tabla
        val headerRow = TableRow(this)
        headerRow.gravity = Gravity.CENTER_HORIZONTAL

        val headerFecha = TextView(this)
        headerFecha.text = "Fecha"
        headerFecha.setPadding(8, 8, 8, 8)
        headerFecha.setTypeface(null, Typeface.BOLD)
        headerFecha.setTextColor(Color.BLACK)
        headerFecha.gravity = Gravity.CENTER
        headerRow.addView(headerFecha)

        val headerMonto = TextView(this)
        headerMonto.text = "Monto"
        headerMonto.setPadding(8, 8, 8, 8)
        headerMonto.setTypeface(null, Typeface.BOLD)
        headerMonto.setTextColor(Color.BLACK)
        headerMonto.gravity = Gravity.CENTER
        headerRow.addView(headerMonto)

        val headerCategoria = TextView(this)
        headerCategoria.text = "Categoría"
        headerCategoria.setPadding(8, 8, 8, 8)
        headerCategoria.setTypeface(null, Typeface.BOLD)
        headerCategoria.setTextColor(Color.BLACK)
        headerCategoria.gravity = Gravity.CENTER
        headerRow.addView(headerCategoria)

        tablaSolicitudes.addView(headerRow)

        for (ingreso in ingresos) {
            val fila = TableRow(this)
            fila.gravity = Gravity.CENTER_HORIZONTAL

            val txtFecha = TextView(this)
            txtFecha.text = ingreso.fecha
            txtFecha.setPadding(8, 8, 8, 8)
            txtFecha.gravity = Gravity.CENTER
            fila.addView(txtFecha)

            val txtMonto = TextView(this)
            txtMonto.text = ingreso.monto.toString()
            txtMonto.setPadding(8, 8, 8, 8)
            txtMonto.gravity = Gravity.CENTER
            fila.addView(txtMonto)

            val txtCategoria = TextView(this)
            txtCategoria.text = ingreso.categoria
            txtCategoria.setPadding(8, 8, 8, 8)
            txtCategoria.gravity = Gravity.CENTER
            fila.addView(txtCategoria)

            tablaSolicitudes.addView(fila)
        }
    }


}
