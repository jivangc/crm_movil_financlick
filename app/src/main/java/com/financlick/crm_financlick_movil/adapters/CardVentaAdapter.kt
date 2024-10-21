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
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.ui.VentaFormActivity
import com.google.gson.Gson

class CardVentaAdapter(private var items: List<CardVentaItem>): RecyclerView.Adapter<CardVentaAdapter.CardVentaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_venta_item, parent, false)
        return CardVentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardVentaViewHolder, position: Int) {
        val ventaItem = items[position]

        //Configura los datos de la tarjeat
        holder.titleView.text = ventaItem.nombreEmpresa
        holder.descriptionView.text = ventaItem.nombreCliente

        //Configura el botón
        holder.buttonView.setOnClickListener{
            val intent = Intent(holder.itemView.context, VentaFormActivity::class.java)
            intent.putExtra("venta", Gson().toJson(ventaItem))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newProducts: List<CardVentaItem>){
        items = newProducts
        notifyDataSetChanged()
    }

    inner class CardVentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
    }
}