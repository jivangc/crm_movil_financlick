package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardQuejaAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.financlick.crm_financlick_movil.models.QuejaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuejasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quejas)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardQuejaAdapter(emptyList())
        recyclerView.adapter = adapter

        // Prueba
        val quejasPrueba: List<CardQuejaItem> = listOf(
            CardQuejaItem(
                titulo = "Queja 1",
                queja = "Descripción de la queja 1"
            ),
            CardQuejaItem(
                titulo = "Sugerencia 1",
                queja = "Descripción de la queja 2"
            ),
            CardQuejaItem(
                titulo = "Queja 2",
                queja = "Descripción de la queja 3"
            ),
            CardQuejaItem(
                titulo = "Sugerencia 2",
                queja = "Descripción de la queja 4"
            ),
            CardQuejaItem(
                titulo = "Queja 3",
                queja = "Descripción de la queja 5"
            ),
            CardQuejaItem(
                titulo = "Sugerencia 3",
                queja = "Descripción de la queja 6"
            ),
        )

        adapter.updateItems(quejasPrueba)

        // Obtener todas las quejas
        //getAllQuejas()
    }

    private fun getAllQuejas() {
        RetrofitClient.instance.getQuejas().enqueue(object : Callback<List<QuejaModel>> {
            override fun onResponse(call: Call<List<QuejaModel>>, response: Response<List<QuejaModel>>) {
                if (response.isSuccessful) {
                    val quejas = response.body()
                    // Manejar las quejas obtenidas
                    Toast.makeText(this@QuejasActivity, "Quejas obtenidas: ${quejas?.size}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuejasActivity, "Error al obtener quejas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<QuejaModel>>, t: Throwable) {
                Toast.makeText(this@QuejasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createQueja(queja: QuejaModel) {
        RetrofitClient.instance.createQueja(queja).enqueue(object : Callback<QuejaModel> {
            override fun onResponse(call: Call<QuejaModel>, response: Response<QuejaModel>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuejasActivity, "Queja creada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuejasActivity, "Error al crear queja", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<QuejaModel>, t: Throwable) {
                Toast.makeText(this@QuejasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun updateQueja(id: Int, queja: QuejaModel) {
        RetrofitClient.instance.updateQueja(id, queja).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuejasActivity, "Queja actualizada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuejasActivity, "Error al actualizar la queja", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@QuejasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteQueja(id: Int) {
        RetrofitClient.instance.deleteQueja(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuejasActivity, "Queja eliminada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuejasActivity, "Error al eliminar la queja", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@QuejasActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}