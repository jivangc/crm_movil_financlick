package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.EmpresaModel
import com.financlick.crm_financlick_movil.models.ProximoPagoModel
import com.financlick.crm_financlick_movil.ui.adapters.PagosAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagosActivity : AppCompatActivity() {
    private lateinit var recyclerPagos: RecyclerView
    private lateinit var spinnerEmpresa: Spinner
    private lateinit var spinnerEstatus: Spinner
    private lateinit var adapter: PagosAdapter
    private val empresaMap = mutableMapOf<Int, String>() // Mapa para nombres de empresas
    private var originalPagos: List<ProximoPagoModel> = emptyList() // Lista original de pagos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos)

        // Configurar RecyclerView
        recyclerPagos = findViewById(R.id.recyclerPagos)
        recyclerPagos.layoutManager = LinearLayoutManager(this)
        adapter = PagosAdapter(emptyList(), empresaMap)
        recyclerPagos.adapter = adapter

        // Configurar Spinners
        spinnerEmpresa = findViewById(R.id.spinnerEmpresa)
        spinnerEstatus = findViewById(R.id.spinnerEstatus)

        // Cargar datos iniciales

        loadEmpresas()
        loadPagos()
        setupFilters()
    }

    private fun loadPagos() {
        RetrofitClient.instance.getProximoPago().enqueue(object : Callback<List<ProximoPagoModel>> {
            override fun onResponse(call: Call<List<ProximoPagoModel>>, response: Response<List<ProximoPagoModel>>) {
                response.body()?.let { pagos ->
                    originalPagos = pagos
                    adapter.updateData(pagos, empresaMap)
                    applyFilters() // Aplicar filtros después de cargar datos
                }
            }

            override fun onFailure(call: Call<List<ProximoPagoModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun loadEmpresas() {
        RetrofitClient.instance.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                response.body()?.let { empresas ->
                    empresas.forEach { empresa ->
                        empresaMap[empresa.idEmpresa] = empresa.nombreEmpresa
                    }
                    val nombresEmpresas = listOf("Todas") + empresas.map { it.nombreEmpresa }
                    spinnerEmpresa.adapter = ArrayAdapter(
                        this@PagosActivity,
                        android.R.layout.simple_spinner_item,
                        nombresEmpresas
                    ).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
                    applyFilters() // Aplicar filtros después de cargar empresas
                }
            }

            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun setupFilters() {
        spinnerEstatus.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Todos", "Paid", "Pending")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerEmpresa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerEstatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters() {
        // Validar que los datos están disponibles antes de filtrar
        if (originalPagos.isEmpty()) return

        val empresaFilter = spinnerEmpresa.selectedItem?.toString() ?: "Todas"
        val statusFilter = spinnerEstatus.selectedItem?.toString() ?: "Todos"

        // Filtrar la lista original
        val filteredPagos = originalPagos.filter {
            val empresaNombre = empresaMap[it.idEmpresa] ?: "Empresa desconocida"
            (empresaFilter == "Todas" || empresaNombre == empresaFilter) &&
                    (statusFilter == "Todos" || it.status == statusFilter)
        }

        // Actualizar los datos del adaptador
        adapter.updateData(filteredPagos, empresaMap)
    }

}
