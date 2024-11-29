package com.financlick.crm_financlick_movil.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.models.ProximoPagoModel

class PagosAdapter(
    var pagos: List<ProximoPagoModel>,
    var empresaMap: MutableMap<Int, String> // Cambiado a MutableMap para permitir actualizaciones
) : RecyclerView.Adapter<PagosAdapter.PagosViewHolder>() {

    inner class PagosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEmpresa: TextView = view.findViewById(R.id.txtEmpresa)
        val txtFecha : TextView = view.findViewById(R.id.txtFecha)
        val txtMonto: TextView = view.findViewById(R.id.txtMonto)
        val statusIndicator: View = view.findViewById(R.id.statusIndicator) // Indicador circular
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pago, parent, false)
        return PagosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {
        val pago = pagos[position]

        // Obtener el nombre de la empresa usando el mapa
        val nombreEmpresa = empresaMap[pago.idEmpresa] ?: "Empresa desconocida"
        holder.txtEmpresa.text = nombreEmpresa

        // Actualizar el texto del monto
        holder.txtMonto.text = "$${pago.amount} MXN"

        if (pago.status == "Paid") {
            holder.txtFecha.text = "Fecha de creacion: ${pago.paidAt}"
        }else{
            holder.txtFecha.text = "Fecha de pago: ${pago.createdAt}"
        }

        // Cambiar el color del indicador de estado
        val statusColor = if (pago.status == "Paid") {
            holder.itemView.context.getColor(R.color.green) // Color para pagos completados

        } else {
            holder.itemView.context.getColor(R.color.red) // Color para pagos pendientes
        }
        holder.statusIndicator.setBackgroundColor(statusColor)
    }

    override fun getItemCount(): Int = pagos.size

    // Actualizar los datos del adaptador
    fun updateData(newPagos: List<ProximoPagoModel>, newEmpresaMap: Map<Int, String>) {
        pagos = newPagos
        empresaMap = newEmpresaMap as MutableMap<Int, String>
        notifyDataSetChanged()
    }



    fun getCurrentPagos(): List<ProximoPagoModel> {
        return pagos
    }
}
