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
import com.financlick.crm_financlick_movil.adapters.CardContactoAddapter
import com.financlick.crm_financlick_movil.adapters.CardQuejaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardContactoItem
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.financlick.crm_financlick_movil.models.PlanificacionModel
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class PanificacionActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panificacion)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardContactoAddapter(emptyList())
        recyclerView.adapter = adapter

        this.getContactos { quejas ->
            adapter.updateItems(quejas)
        }

        floatingButton = findViewById(R.id.addQueja)
        floatingButton.setOnClickListener {
            val intent = Intent(this, PlanificacionFormActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getContactos(onComplete: (List<CardContactoItem>) -> Unit) {
        RetrofitClient.instance.getContactos().enqueue(object: Callback<List<PlanificacionModel>>{
            override fun onResponse(call: Call<List<PlanificacionModel>>, response: Response<List<PlanificacionModel>>) {
                if (response.isSuccessful) {
                    val contactos = response.body() ?: emptyList()
                    if (contactos.isNotEmpty()) {
                        val cardContactos = contactos.map { contacto ->
                            CardContactoItem(
                                idContacto = contacto.idContacto,
                                idEmpresa = contacto.idEmpresa,
                                email = if (contacto.email == null) "" else contacto.email,
                                telefono = if (contacto.telefono == null) "" else contacto.telefono,
                                nombre = contacto.nombre,
                                apellido = contacto.apellido,
                                puesto = contacto.puesto
                            )
                        }
                        onComplete(cardContactos)
                    }else{
                        onComplete(emptyList())
                    }
                }else{
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<PlanificacionModel>>, t: Throwable) {
                // Manejar la falla de la llamada, por ejemplo, mostrar un mensaje de error
                Log.e("Error al llenar los contactos", t.toString())
                onComplete(emptyList())
            }
        })

    }




}