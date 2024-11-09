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
import com.financlick.crm_financlick_movil.items.CardIngresosEgresosItem
import com.financlick.crm_financlick_movil.ui.FinanzasFormActivity
import com.google.gson.Gson

class CardIngresosEgresosAdapter (private var items: List<CardIngresosEgresosItem>) : RecyclerView.Adapter<CardIngresosEgresosAdapter.CardIngresosEgresosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardIngresosEgresosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_ingreso_egreso_item, parent, false)
        return CardIngresosEgresosViewHolder(view)
    }

    inner class CardIngresosEgresosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
    }

    override fun onBindViewHolder(holder: CardIngresosEgresosViewHolder, position: Int) {
        val ingresosEgresosItem = items[position]

        // Configura los datos de la tarjeta
        holder.titleView.text = ingresosEgresosItem.categoria
        holder.descriptionView.text = ingresosEgresosItem.descripcion

        // Configura el botón
        holder.buttonView.setOnClickListener {
            //Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, FinanzasFormActivity::class.java)
            intent.putExtra("ingresosEgresos", Gson().toJson(ingresosEgresosItem))
            Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newIngresosEgresos: List<CardIngresosEgresosItem>) {
        items = newIngresosEgresos
        notifyDataSetChanged()
    }
}