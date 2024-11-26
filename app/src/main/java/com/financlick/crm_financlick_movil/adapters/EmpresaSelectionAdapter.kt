package com.financlick.crm_financlick_movil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.models.EmpresaModel

class EmpresaSelectionAdapter(
    private val empresas: List<EmpresaModel>,
    private val onEmpresaSelected: (EmpresaModel, Boolean) -> Unit
) : RecyclerView.Adapter<EmpresaSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBoxEmpresa)
        val textViewNombre: TextView = view.findViewById(R.id.textViewNombreEmpresa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.selection_empresa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val empresa = empresas[position]
        holder.textViewNombre.text = empresa.nombreEmpresa
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onEmpresaSelected(empresa, isChecked)
        }
    }

    override fun getItemCount() = empresas.size
}