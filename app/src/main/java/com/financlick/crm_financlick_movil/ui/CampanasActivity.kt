package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardCampaniaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardCampaniaItem
import com.financlick.crm_financlick_movil.models.CampaniaModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Callback
import java.util.Date

class CampanasActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_campanas)

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardCampaniaAdapter(emptyList())
        recyclerView.adapter = adapter

        floatingButton = findViewById(R.id.addCampania)
        floatingButton.setOnClickListener {
            val intent = Intent(this, CampaniaFormActivity::class.java)
            startActivity(intent)
            finish()
        }

        this.getCampanias { campanias ->
            adapter.updateItems(campanias)
        }
    }

    private fun getCampanias(onComplete: (List<CardCampaniaItem>) -> Unit) {
        RetrofitClient.instance.getCampanias().enqueue(object: Callback<List<CampaniaModel>> {
            override fun onResponse(call: retrofit2.Call<List<CampaniaModel>>, response: retrofit2.Response<List<CampaniaModel>>) {
                if (response.isSuccessful) {
                    val campanias = response.body() ?: emptyList()
                    if (campanias.isNotEmpty()){
                        val cardCampanias = campanias.map { campania ->
                            CardCampaniaItem(
                                idCampania = campania.idCampania,
                                nombre = campania.nombre,
                                asunto = campania.asunto,
                                contenido = campania.contenido,
                                createdDate = campania.createdDate,
                                scheduleDate = campania.scheduleDate,
                                tipo = campania.tipo,
                                estatus = campania.estatus,
                                destinatarios = campania.destinatarios,
                                idEmpresa = campania.idEmpresa,
                            )
                        }
                        onComplete(cardCampanias)
                    }else{
                        onComplete(emptyList())
                    }
                }else{
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: retrofit2.Call<List<CampaniaModel>>, t: Throwable) {
                Log.d("Error", t.message.toString())
                onComplete(emptyList())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialmente, configura un adaptador vacío
        val adapter = CardCampaniaAdapter(emptyList())
        recyclerView.adapter = adapter

        // Obtener documentos y detalles
        getCampanias { items ->
            // Actualizar la lista de datos y el adaptador
            adapter.updateItems(items)
        }
    }
}