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
import com.financlick.crm_financlick_movil.items.CardQuejaItem
import com.google.gson.Gson

class CardQuejaAdapter(private var items: List<CardQuejaItem>): RecyclerView.Adapter<CardQuejaAdapter.CardQuejaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardQuejaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_queja_item, parent, false)
        return CardQuejaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardQuejaViewHolder, position: Int) {
        val productItem = items[position]

        // Configura los datos de la tarjeta
        holder.titleView.text = productItem.titulo
        holder.descriptionView.text = productItem.queja

        // Configura el botón
        holder.buttonView.setOnClickListener {
            Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newProducts: List<CardQuejaItem>) {
        items = newProducts
        notifyDataSetChanged()
    }

    inner class CardQuejaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
    }
}