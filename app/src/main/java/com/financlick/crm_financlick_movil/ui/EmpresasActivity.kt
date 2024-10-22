package com.financlick.crm_financlick_movil.ui

import android.app.LauncherActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardEmpresaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardEmpresasItem
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.QuejaModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmpresasActivity : AppCompatActivity() {

    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresas)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEmpresas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardEmpresaAdapter(emptyList())
        recyclerView.adapter = adapter

        this.getEmpresas { empresas ->
            adapter.updateItems(empresas)
        }

//        floatingButton = findViewById(R.id.addEmpresa)
//        floatingButton.setOnClickListener {
//            val intent = Intent(this, EmpresaFormActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

    }

    private fun getEmpresas(onComplete: (List<CardEmpresasItem>) -> Unit) {
        RetrofitClient.instance.getEmpresas().enqueue(object: Callback<List<EmpresaModel>>{
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                if (response.isSuccessful) {
                    val empresas = response.body() ?: emptyList()
                    if (empresas.isNotEmpty()) {
                        val cardEmpresas = empresas.map { empresa ->
                            CardEmpresasItem(
                                idEmpresa = empresa.idEmpresa,
                                nombreEmpresa = empresa.nombreEmpresa,
                                fechaConstitucion = empresa.fechaConstitucion.toString(),
                                razonSocial = empresa.razonSocial,
                                logo = empresa.logo
                            )
                        }
                        onComplete(cardEmpresas)
                    }else{
                        onComplete(emptyList())
                    }
                } else{
                    onComplete(emptyList())
                }
            }
            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                // Manejar la falla de la llamada, por ejemplo, mostrar un mensaje de error
                Log.e("PAULOOOOOO", t.toString())
                onComplete(emptyList())
            }
        })
    }
}

