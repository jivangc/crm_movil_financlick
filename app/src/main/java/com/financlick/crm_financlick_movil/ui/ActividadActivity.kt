package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardActividadAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardActividadItem
import com.financlick.crm_financlick_movil.models.ActividadModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActividadActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardActividadAdapter(emptyList())
        recyclerView.adapter = adapter

        this.getActividades { actividades ->
            adapter.updateItems(actividades)
        }

        floatingButton = findViewById(R.id.addActividad)
        floatingButton.setOnClickListener {
            val intent = Intent(this, ActividadFormActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getActividades(onComplete: (List<CardActividadItem>) -> Unit) {
        RetrofitClient.instance.getActividades().enqueue(object : Callback<List<ActividadModel>> {
            override fun onResponse(call: Call<List<ActividadModel>>, response: Response<List<ActividadModel>>) {
                if (response.isSuccessful) {
                    val actividades = response.body() ?: emptyList()
                    if (actividades.isNotEmpty()) {
                        val cardActividades = actividades.map { actividad ->
                            CardActividadItem(
                                idActividad = actividad.idActividad,
                                idUsuario = actividad.idUsuario,
                                nombre = actividad.nombre,
                                descripcion = actividad.descripcion ?: "",
                                estatus = actividad.estatus ?: ""
                            )
                        }
                        onComplete(cardActividades)
                    } else {
                        onComplete(emptyList())
                    }
                } else {
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<ActividadModel>>, t: Throwable) {
                Log.e("Error al cargar actividades", t.toString())
                onComplete(emptyList())
            }
        })
    }
}
