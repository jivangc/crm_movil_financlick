package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import com.financlick.crm_financlick_movil.models.PlanEmpresaModel
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class GenerarVenta : AppCompatActivity() {

    private lateinit var empresaSpinner: Spinner
    private lateinit var empresas: List<EmpresaModel>
    private lateinit var ventaProspecto: CardVentaItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_venta)

        ventaProspecto = intent.getSerializableExtra("VENTA_PROSPECTO") as CardVentaItem

        if (ventaProspecto.idUsuario == null) {
            Toast.makeText(this, "Asigna un usuario primero a la venta", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val idPlan = ventaProspecto.idVPlan
        obtenerPlanPorId(idPlan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Inicializa el Spinner para mostrar las empresas
        empresaSpinner = findViewById(R.id.tipoVentaSpinner)
        cargarEmpresas()

        val descripcionInput: TextInputEditText = findViewById(R.id.descripcionInput)

        // Configuración del botón Guardar
        val btnGuardarVenta: Button = findViewById(R.id.btnGuardarVenta)
        btnGuardarVenta.setOnClickListener {
            val descripcion = descripcionInput.text.toString()
            val empresaSeleccionada = empresas[empresaSpinner.selectedItemPosition]

            if (descripcion.isEmpty()) {
                descripcionInput.error = "La descripción es obligatoria"
                Toast.makeText(this, "La descripción es obligatoria", Toast.LENGTH_SHORT).show()
            } else {
                // Formatear la fecha actual en "yyyy-MM-dd"
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaFormateada = dateFormat.format(Date())

                val nuevoIngreso = IngresosEgresoModel(
                    fecha = fechaFormateada, // Fecha formateada
                    tipoTransaccion = 1,
                    monto = 100.0, // Monto hardcoded
                    descripcion = descripcion,
                    categoria = "Venta",
                    estatus = 1,
                    idEmpresa = empresaSeleccionada.idEmpresa
                )

                guardarIngreso(nuevoIngreso)
            }
        }
    }

    private fun obtenerPlanPorId(idPlan: Int) {
        RetrofitClient.instance.getPlanById(idPlan).enqueue(object : Callback<PlanEmpresaModel> {
            override fun onResponse(call: Call<PlanEmpresaModel>, response: Response<PlanEmpresaModel>) {
                if (response.isSuccessful) {
                    val plan = response.body()
                    val precio = plan?.precio ?: 0.0

                    // Aquí continúa con la lógica de la vista para capturar la descripción y demás
                    configurarBotonGuardarVenta(precio)
                } else {
                    Toast.makeText(this@GenerarVenta, "Error al obtener el plan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlanEmpresaModel>, t: Throwable) {
                Toast.makeText(this@GenerarVenta, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("GenerarVenta", "Error en la conexión: ${t.message}", t)
            }
        })
    }

    private fun configurarBotonGuardarVenta(precio: Double) {
        val descripcionInput: TextInputEditText = findViewById(R.id.descripcionInput)
        val btnGuardarVenta: Button = findViewById(R.id.btnGuardarVenta)

        btnGuardarVenta.setOnClickListener {
            val descripcion = descripcionInput.text.toString()
            val empresaSeleccionada = empresas[empresaSpinner.selectedItemPosition]

            if (descripcion.isEmpty()) {
                descripcionInput.error = "La descripción es obligatoria"
                Toast.makeText(this, "La descripción es obligatoria", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fechaFormateada = dateFormat.format(Date())

                val nuevoIngreso = IngresosEgresoModel(
                    fecha = fechaFormateada,
                    tipoTransaccion = 1,
                    monto = precio,
                    descripcion = descripcion,
                    categoria = "Venta",
                    estatus = 1,
                    idEmpresa = empresaSeleccionada.idEmpresa
                )

                guardarIngreso(nuevoIngreso)
            }
        }
    }

    private fun cargarEmpresas() {
        RetrofitClient.instance.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                if (response.isSuccessful) {
                    empresas = response.body() ?: emptyList()
                    val nombresEmpresas = empresas.map { it.nombreEmpresa }

                    // Configura el Spinner con los nombres de las empresas
                    val adapter = ArrayAdapter(this@GenerarVenta, android.R.layout.simple_spinner_item, nombresEmpresas)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    empresaSpinner.adapter = adapter
                } else {
                    Toast.makeText(this@GenerarVenta, "Error al obtener las empresas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                Toast.makeText(this@GenerarVenta, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guardarIngreso(ingreso: IngresosEgresoModel) {
        val idVentaProspecto = ventaProspecto.idVenta

        RetrofitClient.instance.crearIngreso(ingreso, idVentaProspecto).enqueue(object : Callback<IngresosEgresoModel> {
            override fun onResponse(call: Call<IngresosEgresoModel>, response: Response<IngresosEgresoModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@GenerarVenta, "Ingreso guardado correctamente", Toast.LENGTH_SHORT).show()

                    setResult(RESULT_OK)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@GenerarVenta, "Error al guardar el ingreso: $errorBody", Toast.LENGTH_LONG).show()
                    Log.e("GenerarVenta", "Error al guardar el ingreso: $errorBody")
                }
            }

            override fun onFailure(call: Call<IngresosEgresoModel>, t: Throwable) {
                Toast.makeText(this@GenerarVenta, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("GenerarVenta", "Error en la conexión: ${t.message}", t)
            }
        })
    }

}
