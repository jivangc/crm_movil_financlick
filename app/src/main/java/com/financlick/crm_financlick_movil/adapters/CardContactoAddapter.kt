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
import com.financlick.crm_financlick_movil.items.CardContactoItem
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.ui.PlanificacionFormActivity
import com.financlick.crm_financlick_movil.ui.QuejaFormActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class CardContactoAddapter(private var items: List<CardContactoItem>): RecyclerView.Adapter<CardContactoAddapter.CardContactoViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardContactoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_contacto_item, parent, false)
        return CardContactoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardContactoViewHolder, position: Int) {
        val contactoItem = items[position]
        val idEmpresa = contactoItem.idEmpresa

        // Configura los datos iniciales de la tarjeta con un nombre de empresa temporal o vacío
        holder.titleView.text = "${contactoItem.nombre} ${contactoItem.apellido} (Cargando...)"
        holder.descriptionView.text = "Tel: ${contactoItem.telefono} Email: ${contactoItem.email}"

        // Obtén el nombre de la empresa de forma asíncrona
        getEmpresas { empresas ->
            val empresaNombre = empresas.find { it.idEmpresa == idEmpresa }?.nombreEmpresa ?: "Sin Empresa"

            // Actualiza el título de la tarjeta una vez que tengas el nombre de la empresa
            holder.titleView.text = "${contactoItem.nombre} ${contactoItem.apellido} ($empresaNombre)"
        }

        // Configura el botón
        holder.buttonView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlanificacionFormActivity::class.java)
            intent.putExtra("contacto", Gson().toJson(contactoItem))
            Toast.makeText(it.context, "Botón pulsado", Toast.LENGTH_SHORT).show()
            intent.putExtra("idEmpresa", idEmpresa)
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

    private fun getEmpresas(onComplete: (List<EmpresaModel>) -> Unit) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        RetrofitClient.instance.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                if (response.isSuccessful) {
                    val empresas = response.body() ?: emptyList()
                    if (empresas.isNotEmpty()) {
                        onComplete(empresas)
                    } else {
                        onComplete(emptyList())
                    }
                } else {
                    onComplete(emptyList())
                }
            }

            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                // Manejar la falla de la llamada, por ejemplo, mostrar un mensaje de error
                Log.e("ERROR", t.toString())
                onComplete(emptyList())
            }
        })
    }
}