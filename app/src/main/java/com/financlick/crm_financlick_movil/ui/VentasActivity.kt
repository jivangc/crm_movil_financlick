package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardVentaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.models.VentasModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class VentasActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardVentaAdapter(emptyList())
        recyclerView.adapter = adapter

        this.getVentas { ventas ->
            adapter.updateItems(ventas)
        }

        floatingButton = findViewById(R.id.addVenta)
        floatingButton.setOnClickListener{
            val intent = Intent(this, VentaFormActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getVentas(onComplete: (List<CardVentaItem>) -> Unit){
        RetrofitClient.instance.getVentas().enqueue(object: Callback<List<VentasModel>>{
            override fun onResponse(call: Call<List<VentasModel>>, response: Response<List<VentasModel>>){
                if(response.isSuccessful){
                    val ventas = response.body() ?: emptyList()
                    if(ventas.isNotEmpty()){
                        val cardVentas = ventas.map { venta ->
                            CardVentaItem(
                                idVenta = venta.idVenta,
                                idPlan = venta.idVenta,
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
                    }else{
                        onComplete(emptyList())
                    }
                }else{
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<VentasModel>>, t: Throwable){
                //Manejar fallas de llamadas
                Log.e("ERRORRR", t.toString())
                onComplete(emptyList())
            }
        })
    }
}