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
import com.financlick.crm_financlick_movil.adapters.CardCampaniaAdapter
import com.financlick.crm_financlick_movil.adapters.CardQuejaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class QuejasActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quejas)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardQuejaAdapter(emptyList())
        recyclerView.adapter = adapter

        this.getQuejas { quejas ->
            adapter.updateItems(quejas)
        }

        floatingButton = findViewById(R.id.addQueja)
        floatingButton.setOnClickListener {
            val intent = Intent(this, QuejaFormActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getQuejas(onComplete: (List<CardQuejaItem>) -> Unit) {
        RetrofitClient.instance.getQuejas().enqueue(object: Callback<List<QuejaModel>>{
            override fun onResponse(call: Call<List<QuejaModel>>, response: Response<List<QuejaModel>>) {
                if (response.isSuccessful) {
                    val quejas = response.body() ?: emptyList()
                    if (quejas.isNotEmpty()) {
                        val cardQuejas = quejas.map { queja ->
                            CardQuejaItem(
                                idQuejaSugerencia = queja.idQuejaSugerencia,
                                idEmpresa = queja.idEmpresa,
                                tipo = queja.tipo,
                                descripcion = queja.descripcion,
                                fechaRegistro = queja.fechaRegistro,
                                estatus = queja.estado,
                                fechaResolucion = queja.fechaResolucion,
                                responsable = queja.responsable,
                                prioridad = queja.prioridad,
                                comentarios = queja.comentarios,
                                archivoAdjunto = ""
                            )
                        }
                        onComplete(cardQuejas)
                    }else{
                        onComplete(emptyList())
                    }
                }else{
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<QuejaModel>>, t: Throwable) {
                onComplete(emptyList())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialmente, configura un adaptador vacío
        val adapter = CardQuejaAdapter(emptyList())
        recyclerView.adapter = adapter

        // Obtener documentos y detalles
        getQuejas { items ->
            // Actualizar la lista de datos y el adaptador
            adapter.updateItems(items)
        }
    }

}