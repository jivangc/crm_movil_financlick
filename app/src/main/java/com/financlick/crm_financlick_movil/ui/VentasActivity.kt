package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardVentaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.models.VentasModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class VentasActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardVentaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        // Configurar RecyclerView y Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CardVentaAdapter(emptyList()) { venta -> onVentaSelected(venta) }
        recyclerView.adapter = adapter

        // Cargar las ventas
        getVentas { ventas ->
            adapter.updateItems(ventas)
        }

        // Configurar FloatingActionButton
        floatingButton = findViewById(R.id.addVenta)
        floatingButton.setOnClickListener {
            // Abrir `VentaFormActivity` sin una venta específica (puedes ajustar esto si tienes otro flujo en mente)
            val intent = Intent(this, VentaFormActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Método para obtener ventas y actualizar el RecyclerView.
     */
    private fun getVentas(onComplete: (List<CardVentaItem>) -> Unit) {
        RetrofitClient.instance.getVentas().enqueue(object : Callback<List<VentasModel>> {
            override fun onResponse(call: Call<List<VentasModel>>, response: Response<List<VentasModel>>) {
                if (response.isSuccessful) {
                    val ventas = response.body() ?: emptyList()
                    Log.d("VentasActivity", "Respuesta completa: ${response.body()}")
                    ventas.forEach { venta ->
                        Log.d("VentasActivity", "Venta ID: ${venta.idVenta}, idPlan: ${venta.idVPlan}")
                    }
                    if (ventas.isNotEmpty()) {
                        val cardVentas = ventas.map { venta ->
                            CardVentaItem(
                                idVenta = venta.idVenta,
                                idVPlan = venta.idVPlan,
                                idUsuario = venta.idUsuario,
                                fechaSolicitud = venta.fechaSolicitud,
                                nombreCliente = venta.nombreCliente,
                                nombreEmpresa = venta.nombreEmpresa,
                                numeroContacto = venta.numeroContacto,
                                correo = venta.correo,
                                domicilio = venta.domicilio,
                                ciudad = venta.ciudad,
                                estado = venta.estado,
                                rfc = venta.rfc
                            )
                        }
                        onComplete(cardVentas)
                    } else {
                        Toast.makeText(this@VentasActivity, "No se encontraron ventas.", Toast.LENGTH_SHORT).show()
                        onComplete(emptyList())
                    }
                } else {
                    Toast.makeText(this@VentasActivity, "Error al obtener ventas: ${response.code()}", Toast.LENGTH_SHORT).show()
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<VentasModel>>, t: Throwable) {
                Log.e("VentasActivity", "Error en la solicitud: ${t.message}")
                Toast.makeText(this@VentasActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                onComplete(emptyList())
            }
        })
    }

    private fun onVentaSelected(venta: CardVentaItem) {
        Log.d("VentasActivity", "Seleccionando venta: $venta")

        val intent = Intent(this, VentaFormActivity::class.java)
        intent.putExtra("VENTA_ACTUAL", venta)
        startActivity(intent)
    }

}
