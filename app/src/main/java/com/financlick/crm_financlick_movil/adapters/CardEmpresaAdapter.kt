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
import com.financlick.crm_financlick_movil.items.CardEmpresasItem
import com.financlick.crm_financlick_movil.ui.EmpresaDetailActivity
import com.financlick.crm_financlick_movil.ui.EmpresaFormActivity
import com.google.gson.Gson

class CardEmpresaAdapter (private var items: List<CardEmpresasItem>): RecyclerView.Adapter<CardEmpresaAdapter.CardEmpresaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardEmpresaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_empresa_item, parent, false)
        return CardEmpresaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardEmpresaViewHolder, position: Int) {
        val empresaItem = items[position]

        // Configura los datos de la tarjeta
        holder.titleView.text = empresaItem.nombreEmpresa
        holder.descriptionView.text = empresaItem.razonSocial

        // Configura el botón
        holder.buttonView.setOnClickListener {
            //Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, EmpresaFormActivity::class.java)
            empresaItem.logo = ""
            intent.putExtra("empresa", Gson().toJson(empresaItem))
            //Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            holder.itemView.context.startActivity(intent)
        }

        holder.buttonContactos.setOnClickListener{
            val intent = Intent(holder.itemView.context, EmpresaDetailActivity::class.java)
            intent.putExtra("idEmpresa", empresaItem.idEmpresa)
            intent.putExtra("nombreEmpresa", empresaItem.nombreEmpresa)
            holder.itemView.context.startActivity(intent)

        }
    }


    override fun getItemCount(): Int = items.size

    fun updateItems(newEmpresas: List<CardEmpresasItem>) {
        items = newEmpresas
        notifyDataSetChanged()
    }

    inner class CardEmpresaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
        val buttonContactos: Button = view.findViewById(R.id.contactosButton)
    }
}