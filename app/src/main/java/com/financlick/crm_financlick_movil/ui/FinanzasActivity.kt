package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardIngresosEgresosAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardIngresosEgresosItem
import com.financlick.crm_financlick_movil.models.IngresoEgresoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Locale
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class FinanzasActivity : AppCompatActivity() {

    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ingreso_egreso)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFinanzas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardIngresosEgresosAdapter(emptyList())
        recyclerView.adapter = adapter

        this.getIngresosEgresos { ingresoEgresos ->
            adapter.updateItems(ingresoEgresos)
        }


        floatingButton = findViewById(R.id.addIngresoEgreso)
        floatingButton.setOnClickListener {
            val intent = Intent(this, FinanzasFormActivity::class.java)
            startActivity(intent)
            finish()
        }




    }

    private fun getIngresosEgresos(onComplete: (List<CardIngresosEgresosItem>) -> Unit) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    RetrofitClient.instance.getIngresosEgresos().enqueue(object : Callback<List<IngresoEgresoModel>> {
        override fun onResponse(call: Call<List<IngresoEgresoModel>>, response: Response<List<IngresoEgresoModel>>) {
            Log.i("FinanzasActivity", "Response: $response")
            if (response.isSuccessful) {
                val ingresosEgresos = response.body() ?: emptyList()
                if (ingresosEgresos.isNotEmpty()) {
                    val cardIngresosEgresos = ingresosEgresos.map { ingresoEgreso ->
                        CardIngresosEgresosItem(
                            idIngresosEgresos = ingresoEgreso.idIngresosEgresos.toString(),
                            fecha = ingresoEgreso.fecha?.let { dateFormat.format(it) } ?: "",
                            tipoTransaccion = ingresoEgreso.tipoTransaccion.toString(),
                            monto = ingresoEgreso.monto.toString(),
                            descripcion = ingresoEgreso.descripcion.toString(),
                            categoria = ingresoEgreso.categoria.toString(),
                            estatus = ingresoEgreso.estatus
                        )
                    }
                    onComplete(cardIngresosEgresos)
                } else {
                    onComplete(emptyList())
                }
            } else {
                onComplete(emptyList())
            }
        }

        override fun onFailure(call: Call<List<IngresoEgresoModel>>, t: Throwable) {
            Log.e("ERROR", t.toString())
            onComplete(emptyList())
        }
    })
}
}