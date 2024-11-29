package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardEgresoAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoEgresosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardEgresoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_egresos)

        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        recyclerView = findViewById(R.id.recyclerViewEgresos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CardEgresoAdapter(
            emptyList(),
            onEditEgreso = { egreso ->
                val intent = Intent(this, EgresosActivity::class.java)
                intent.putExtra("egreso", egreso)
                startActivity(intent)
            },
            onDeleteEgreso = { egreso ->
                //deleteEgreso(egreso)
            }
        )
        recyclerView.adapter = adapter

        val fabAgregarEgreso = findViewById<FloatingActionButton>(R.id.fabAgregarEgreso)
        fabAgregarEgreso.setOnClickListener {
            val intent = Intent(this, EgresosActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadEgresosFromApi()
    }

    private fun loadEgresosFromApi() {
        RetrofitClient.instance.getEgresos().enqueue(object : Callback<List<IngresosEgresoModel>> {
            override fun onResponse(
                call: Call<List<IngresosEgresoModel>>,
                response: Response<List<IngresosEgresoModel>>
            ) {
                if (response.isSuccessful) {
                    val egresos = response.body()
                    if (egresos != null) {
                        adapter.updateItems(egresos)
                    }
                } else {
                    Toast.makeText(
                        this@InfoEgresosActivity,
                        "Error al obtener los egresos: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<IngresosEgresoModel>>, t: Throwable) {
                Log.e("InfoEgresosActivity", "Error al conectar con la API", t)
                Toast.makeText(
                    this@InfoEgresosActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
