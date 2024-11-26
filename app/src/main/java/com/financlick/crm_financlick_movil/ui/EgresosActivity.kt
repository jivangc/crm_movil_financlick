package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.ui.InfoEgresosActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.AlertDialog
import android.view.View

class EgresosActivity : AppCompatActivity() {
    private var egresoExistente: IngresosEgresoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egresos)

        val montoEditText = findViewById<EditText>(R.id.monto)
        val descripcionEditText = findViewById<EditText>(R.id.descripcion)
        val agregarButton = findViewById<Button>(R.id.btnAgregar)
        val eliminarButton = findViewById<Button>(R.id.btnEliminar)

        // Obtener el egreso enviado desde InfoEgresosActivity
        egresoExistente = intent.getSerializableExtra("egreso") as? IngresosEgresoModel

        // Si hay un egreso existente, prellenar los campos y mostrar el botón de eliminar
        egresoExistente?.let { egreso ->
            montoEditText.setText(egreso.monto.toString())
            descripcionEditText.setText(egreso.descripcion)
            agregarButton.text = "Actualizar"
            eliminarButton.visibility = View.VISIBLE
        }

        // Lógica para agregar o actualizar
        agregarButton.setOnClickListener {
            val montoText = montoEditText.text.toString()
            val descripcionText = descripcionEditText.text.toString()

            if (montoText.isEmpty() || descripcionText.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto: Double
            try {
                monto = montoText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Por favor ingresa un monto válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val egreso = egresoExistente?.copy(
                monto = monto,
                descripcion = descripcionText,
                fecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
                estatus = 1
            ) ?: IngresosEgresoModel(
                fecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date()),
                tipoTransaccion = 2,
                monto = monto,
                descripcion = descripcionText,
                categoria = "Egreso",
                estatus = 1,
                idEmpresa = null
            )

            if (egresoExistente != null) {
                actualizarEgreso(egreso)
            } else {
                enviarEgreso(egreso)
            }
        }

        // Lógica para eliminar
        eliminarButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este egreso?")
                .setPositiveButton("Sí") { _, _ ->
                    eliminarEgreso()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun enviarEgreso(egreso: IngresosEgresoModel) {
        RetrofitClient.instance.crearEgreso(egreso).enqueue(object : Callback<IngresosEgresoModel> {
            override fun onResponse(call: Call<IngresosEgresoModel>, response: Response<IngresosEgresoModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EgresosActivity, "Egreso agregado correctamente.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EgresosActivity, "Error al agregar egreso: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<IngresosEgresoModel>, t: Throwable) {
                Toast.makeText(this@EgresosActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarEgreso(egreso: IngresosEgresoModel) {
        egresoExistente?.let { existing ->
            RetrofitClient.instance.actualizarEgreso(existing.idIngresosEgresos ?: 0, egreso)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EgresosActivity, "Egreso actualizado correctamente.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@EgresosActivity, "Error al actualizar egreso: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@EgresosActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun eliminarEgreso() {
        egresoExistente?.let { existing ->
            val fechaActual = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
            val egresoEliminado = existing.copy(
                estatus = 0,
                fecha = fechaActual
            )
            actualizarEgreso(egresoEliminado)
        }
    }
}
