package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardContactoAddapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardContactoItem
import com.financlick.crm_financlick_movil.models.PlanificacionModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmpresaDetailActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton
    private var idEmpresa: Int = 0
    private lateinit var nombreEmpresa: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_detail)

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        idEmpresa = intent.getIntExtra("idEmpresa", 0)
        nombreEmpresa = intent.getStringExtra("nombreEmpresa") ?: ""

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardContactoAddapter(emptyList())
        recyclerView.adapter = adapter

        this.getContactos(idEmpresa) { contactos ->
            adapter.updateItems(contactos)
        }

        floatingButton = findViewById(R.id.addContacto)
        floatingButton.setOnClickListener {
            val intent = Intent(this, PlanificacionFormActivity::class.java)
            intent.putExtra("idEmpresa", idEmpresa)
            startActivity(intent)
        }
    }

    private fun getContactos(idEmpresa: Int, onComplete: (List<CardContactoItem>) -> Unit) {
        RetrofitClient.instance.getContactosPorEmpresa(idEmpresa).enqueue(object : Callback<List<PlanificacionModel>> {
            override fun onResponse(call: Call<List<PlanificacionModel>>, response: Response<List<PlanificacionModel>>) {
                if (response.isSuccessful) {
                    val contactos = response.body() ?: emptyList()
                    if (contactos.isNotEmpty()) {
                        val cardContactos = contactos.map { contacto ->
                            CardContactoItem(
                                idContacto = contacto.idContacto,
                                idEmpresa = contacto.idEmpresa,
                                email = contacto.email ?: "",
                                telefono = contacto.telefono ?: "",
                                nombre = contacto.nombre,
                                apellido = contacto.apellido,
                                puesto = contacto.puesto ?: ""
                            )
                        }
                        onComplete(cardContactos)
                    } else {
                        onComplete(emptyList())
                    }
                } else {
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<PlanificacionModel>>, t: Throwable) {
                Log.e("Error al obtener contactos", t.toString())
                onComplete(emptyList())
            }
        })
    }
}