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
import com.financlick.crm_financlick_movil.items.CardContactoItem
import com.financlick.crm_financlick_movil.ui.PlanificacionFormActivity
import com.financlick.crm_financlick_movil.ui.QuejaFormActivity
import com.google.gson.Gson

class CardContactoAddapter(private var items: List<CardContactoItem>): RecyclerView.Adapter<CardContactoAddapter.CardContactoViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardContactoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_contacto_item, parent, false)
        return CardContactoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardContactoViewHolder, position: Int) {
        val contactoItem = items[position]

        // Configura los datos de la tarjeta
        holder.titleView.text = contactoItem.nombre + ' ' + contactoItem.apellido
        holder.descriptionView.text = "Tel: "+ contactoItem.telefono + " Email: "+ contactoItem.email
        //holder.descriptionView.text = quejaItem.descripcion

        // Configura el botón
        holder.buttonView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlanificacionFormActivity::class.java)
            intent.putExtra("contacto", Gson().toJson(contactoItem))
            Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newProducts: List<CardContactoItem>) {
        items = newProducts
        notifyDataSetChanged()
    }

    inner class CardContactoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.cardTitle)
        val descriptionView: TextView = view.findViewById(R.id.cardDescription)
        val buttonView: Button = view.findViewById(R.id.cardButton)
    }
}