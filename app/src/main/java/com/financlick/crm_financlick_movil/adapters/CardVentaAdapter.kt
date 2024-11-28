package com.financlick.crm_financlick_movil.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.items.CardVentaItem
import com.financlick.crm_financlick_movil.ui.GenerarVenta
import com.google.gson.Gson

class CardVentaAdapter(
    private var items: List<CardVentaItem>,
    private val onVentaClick: (CardVentaItem) -> Unit
) : RecyclerView.Adapter<CardVentaAdapter.CardVentaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardVentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_venta_item, parent, false)
        return CardVentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardVentaViewHolder, position: Int) {
        val ventaItem = items[position]

        holder.titleView.text = ventaItem.nombreEmpresa
        holder.descriptionView.text = ventaItem.nombreCliente

        holder.buttonView.setOnClickListener {
            onVentaClick(ventaItem)
        }

        holder.buttonGenerarVenta.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, GenerarVenta::class.java)
            intent.putExtra("VENTA_PROSPECTO", Gson().toJson(ventaItem))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CardVentaItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class CardVentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
        val buttonGenerarVenta: Button = view.findViewById(R.id.cardButtonGenerate)
    }
}
