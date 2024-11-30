package com.financlick.crm_financlick_movil.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.items.CardActividadItem
import com.financlick.crm_financlick_movil.models.UsuarioModel
import com.financlick.crm_financlick_movil.ui.ActividadFormActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class CardActividadAdapter(private var items: List<CardActividadItem>) : RecyclerView.Adapter<CardActividadAdapter.CardActividadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_actividad_item, parent, false)
        return CardActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardActividadViewHolder, position: Int) {
        val actividadItem = items[position]
        val idUsuario = actividadItem.idUsuario

        // Configura los datos iniciales de la tarjeta con un nombre de usuario temporal o vacío
        holder.titleView.text = "${actividadItem.nombre}"
        holder.descriptionView.text = "Descripción: ${actividadItem.descripcion} Estatus: ${actividadItem.estatus}"

        // Obtén el nombre del usuario de forma asíncrona
        /*getUsuarios { usuarios ->
            val usuarioNombre = usuarios.find { it.idUsuario == idUsuario }?.nombre ?: "Sin Usuario"

            // Actualiza el título de la tarjeta una vez que tengas el nombre del usuario
            holder.titleView.text = "${actividadItem.nombre} ($usuarioNombre)"
        }*/

        // Configura el botón
        holder.buttonView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ActividadFormActivity::class.java)
            intent.putExtra("actividad", Gson().toJson(actividadItem))
            //Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CardActividadItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class CardActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
    }

    /*private fun getUsuarios(onComplete: (List<UsuarioModel>) -> Unit) {
        RetrofitClient.instance.getUsuarios().enqueue(object : Callback<List<UsuarioModel>> {
            override fun onResponse(call: Call<List<UsuarioModel>>, response: Response<List<UsuarioModel>>) {
                if (response.isSuccessful) {
                    val usuarios = response.body() ?: emptyList()
                    onComplete(usuarios)
                } else {
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<UsuarioModel>>, t: Throwable) {
                Log.e("ERROR", t.toString())
                onComplete(emptyList())
            }
        })
    }*/
}
