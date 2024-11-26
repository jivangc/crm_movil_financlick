package com.financlick.crm_financlick_movil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.models.IngresosEgresoModel

class CardEgresoAdapter(
    private var items: List<IngresosEgresoModel>,
    private val onEditEgreso: (IngresosEgresoModel) -> Unit,
    private val onDeleteEgreso: (IngresosEgresoModel) -> Unit
) : RecyclerView.Adapter<CardEgresoAdapter.CardEgresoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardEgresoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_egreso_item, parent, false)
        return CardEgresoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardEgresoViewHolder, position: Int) {
        val egresoItem = items[position]

        holder.descriptionView.text = egresoItem.descripcion
        holder.montoView.text = "Monto: $${egresoItem.monto}"
        holder.fechaView.text = "Fecha: ${egresoItem.fecha}"

        holder.editButton.setOnClickListener {
            onEditEgreso(egresoItem)
        }

    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<IngresosEgresoModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class CardEgresoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val montoView: TextView = view.findViewById(R.id.cardMonto)
        val fechaView: TextView = view.findViewById(R.id.cardDate)
        val editButton: Button = view.findViewById(R.id.cardButtonEdit)
    }
}
