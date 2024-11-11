package com.financlick.crm_financlick_movil.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.items.CardCampaniaItem
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.financlick.crm_financlick_movil.ui.CampaniaFormActivity
import com.financlick.crm_financlick_movil.ui.QuejaFormActivity
import com.google.gson.Gson

class CardCampaniaAdapter (private var items: List<CardCampaniaItem>): RecyclerView.Adapter<CardCampaniaAdapter.CardCampaniaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardCampaniaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_campania_item, parent, false)
        return CardCampaniaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardCampaniaViewHolder, position: Int) {
        val campaniaItem = items[position]

        // Configura los datos de la tarjeta
        holder.tipoView.text = campaniaItem.tipo
        holder.descriptionView.text = campaniaItem.asunto
        when (campaniaItem.estatus) {
            1 -> holder.statusView.text = "Pendiente"
            2 -> holder.statusView.text = "Enviado"
            3 -> holder.statusView.text = "Cerrado"
            4 -> holder.statusView.text = "Enviado Parcial"
        }

        // Configura el botón
        holder.buttonView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CampaniaFormActivity::class.java)
            intent.putExtra("campania", Gson().toJson(campaniaItem))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newCampanias: List<CardCampaniaItem>) {
        items = newCampanias
        notifyDataSetChanged()
    }

    inner class CardCampaniaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tipoView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val statusView: TextView = view.findViewById(R.id.cardStatus)
        val buttonView: Button = view.findViewById(R.id.cardButton)
    }

}