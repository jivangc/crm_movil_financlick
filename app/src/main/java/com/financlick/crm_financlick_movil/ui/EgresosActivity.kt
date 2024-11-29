package com.financlick.crm_financlick_movil.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EgresosActivity : AppCompatActivity() {
    private var egresoExistente: IngresosEgresoModel? = null
    private lateinit var montoEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var agregarButton: FloatingActionButton
    private lateinit var eliminarButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egresos)

        montoEditText = findViewById(R.id.monto)
        descripcionEditText = findViewById(R.id.descripcion)
        agregarButton = findViewById(R.id.btnAgregar)
        eliminarButton = findViewById(R.id.btnEliminar)


        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        egresoExistente = intent.getSerializableExtra("egreso") as? IngresosEgresoModel

        egresoExistente?.let { egreso ->
            montoEditText.setText(egreso.monto.toString())
            descripcionEditText.setText(egreso.descripcion)
            //agregarButton.text = "Actualizar"
            eliminarButton.visibility = View.VISIBLE
        }

        agregarButton.setOnClickListener {
            if (validateFields()) {
                val monto = montoEditText.text.toString().toDoubleOrNull() ?: 0.0
                val descripcion = descripcionEditText.text.toString()

                val egreso = egresoExistente?.copy(
                    monto = monto,
                    descripcion = descripcion,
                    fecha = getCurrentDate(),
                    estatus = 1
                ) ?: IngresosEgresoModel(
                    fecha = getCurrentDate(),
                    tipoTransaccion = 2,
                    monto = monto,
                    descripcion = descripcion,
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
        }

        eliminarButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (montoEditText.text.isNullOrBlank()) {
            montoEditText.error = "Por favor ingresa un monto"
            isValid = false
        } else {
            val monto = montoEditText.text.toString().toDoubleOrNull()
            if (monto == null || monto <= 0) {
                montoEditText.error = "Por favor ingresa un monto válido mayor a 0"
                isValid = false
            }
        }

        if (descripcionEditText.text.isNullOrBlank()) {
            descripcionEditText.error = "Por favor ingresa una descripción"
            isValid = false
        }

        return isValid
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
            val egresoEliminado = existing.copy(
                estatus = 0,
                fecha = getCurrentDate()
            )
            actualizarEgreso(egresoEliminado)
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este egreso?")
            .setPositiveButton("Sí") { _, _ ->
                eliminarEgreso()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}