package com.financlick.crm_financlick_movil.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.EmpresaSelectionAdapter
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.api.ServiceApi
import com.financlick.crm_financlick_movil.models.CampaniaModel
import com.financlick.crm_financlick_movil.models.EmpresaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SolicitudesCampaniasActivity : AppCompatActivity() {
    private lateinit var spinnerCampanias: Spinner
    private lateinit var recyclerViewEmpresas: RecyclerView
    private lateinit var btnEnviarSolicitud: Button
    private lateinit var progressBar: ProgressBar

    private var selectedCampaniaId: Int = -1
    private val selectedEmpresasIds = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_campanias)

        spinnerCampanias = findViewById(R.id.spinnerCampanias)
        recyclerViewEmpresas = findViewById(R.id.recyclerViewEmpresas)
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud)
        progressBar = findViewById(R.id.progressBar)

        setupCampaniasSpinner()
        setupEmpresasRecyclerView()

        btnEnviarSolicitud.setOnClickListener {
            if (selectedCampaniaId != -1 && selectedEmpresasIds.isNotEmpty()) {
                sendEmailsRequest()
            } else {
                Toast.makeText(this, "Por favor, seleccione una campaña y al menos una empresa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCampaniasSpinner() {
        RetrofitClient.instance.getCampanias().enqueue(object : Callback<List<CampaniaModel>> {
            override fun onResponse(call: Call<List<CampaniaModel>>, response: Response<List<CampaniaModel>>) {
                if (response.isSuccessful) {
                    val campanias = response.body() ?: emptyList()
                    val adapter = ArrayAdapter(this@SolicitudesCampaniasActivity, android.R.layout.simple_spinner_item, campanias.map { it.nombre })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCampanias.adapter = adapter

                    spinnerCampanias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            selectedCampaniaId = campanias[position].idCampania
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            selectedCampaniaId = -1
                        }
                    }
                } else {
                    Toast.makeText(this@SolicitudesCampaniasActivity, "Error al cargar las campañas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CampaniaModel>>, t: Throwable) {
                Toast.makeText(this@SolicitudesCampaniasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupEmpresasRecyclerView() {
        recyclerViewEmpresas.layoutManager = LinearLayoutManager(this)
        RetrofitClient.instance.getEmpresas().enqueue(object : Callback<List<EmpresaModel>> {
            override fun onResponse(call: Call<List<EmpresaModel>>, response: Response<List<EmpresaModel>>) {
                if (response.isSuccessful) {
                    val empresas = response.body() ?: emptyList()
                    val adapter = EmpresaSelectionAdapter(empresas) { empresa, isSelected ->
                        if (isSelected) {
                            selectedEmpresasIds.add(empresa.idEmpresa)
                        } else {
                            selectedEmpresasIds.remove(empresa.idEmpresa)
                        }
                    }
                    recyclerViewEmpresas.adapter = adapter
                } else {
                    Toast.makeText(this@SolicitudesCampaniasActivity, "Error al cargar las empresas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<EmpresaModel>>, t: Throwable) {
                Toast.makeText(this@SolicitudesCampaniasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendEmailsRequest() {
        progressBar.visibility = View.VISIBLE
        val emailsBody = ServiceApi.EmailsBody(selectedEmpresasIds)
        RetrofitClient.instance.sendMails(selectedCampaniaId, emailsBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@SolicitudesCampaniasActivity, "Solicitud enviada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SolicitudesCampaniasActivity, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@SolicitudesCampaniasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}