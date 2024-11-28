package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.AcumuladoAnualModel
import com.financlick.crm_financlick_movil.models.TotalesMensualesModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinanzasActivity : AppCompatActivity() {

    private lateinit var chartTotalesMensuales: LineChart
    private lateinit var chartAcumuladoAnual: PieChart
    private lateinit var pagosButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finanzas)

        chartTotalesMensuales = findViewById(R.id.chartTotalesMensuales)
        chartAcumuladoAnual = findViewById(R.id.chartAcumuladoAnual)
        pagosButton = findViewById(R.id.pagosButton)

        pagosButton.setOnClickListener {
            val intent = Intent(this, PagosActivity::class.java)
            startActivity(intent)
        }

        // Cargar datos para las gráficas
        loadTotalesMensuales()
        loadAcumuladoAnual()
    }

    private fun loadTotalesMensuales() {
        RetrofitClient.instance.getTotales().enqueue(object : Callback<List<TotalesMensualesModel>> {
            override fun onResponse(
                call: Call<List<TotalesMensualesModel>>,
                response: Response<List<TotalesMensualesModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        val ingresosEntries = data.map {
                            Entry(it.mes.toFloat(), it.totalIngresos)
                        }
                        val egresosEntries = data.map {
                            Entry(it.mes.toFloat(), it.totalEgresos)
                        }

                        val ingresosDataSet = LineDataSet(ingresosEntries, "Ingresos")
                        val egresosDataSet = LineDataSet(egresosEntries, "Egresos")

                        chartTotalesMensuales.data = LineData(ingresosDataSet, egresosDataSet)
                        chartTotalesMensuales.invalidate()
                    }
                } else {
                    Toast.makeText(this@FinanzasActivity, "Error al cargar totales mensuales", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TotalesMensualesModel>>, t: Throwable) {
                Toast.makeText(this@FinanzasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadAcumuladoAnual() {
        RetrofitClient.instance.getAcumuladoAnual().enqueue(object : Callback<List<AcumuladoAnualModel>> {
            override fun onResponse(
                call: Call<List<AcumuladoAnualModel>>,
                response: Response<List<AcumuladoAnualModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        val ingresosSum = data.sumOf { it.ingresosAcumulados.toDouble() }
                        val egresosSum = data.sumOf { it.egresosAcumulados.toDouble() }


                        val pieEntries = listOf(
                            PieEntry(ingresosSum.toFloat(), "Ingresos"),
                            PieEntry(egresosSum.toFloat(), "Egresos")
                        )

                        val dataSet = PieDataSet(pieEntries, "Acumulado Anual")
                        dataSet.colors = listOf(
                            ColorTemplate.COLORFUL_COLORS[0],
                            ColorTemplate.COLORFUL_COLORS[1]
                        )

                        val pieData = PieData(dataSet)
                        chartAcumuladoAnual.data = pieData
                        chartAcumuladoAnual.invalidate()
                    }
                } else {
                    Toast.makeText(this@FinanzasActivity, "Error al cargar acumulado anual", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<AcumuladoAnualModel>>, t: Throwable) {
                Toast.makeText(this@FinanzasActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
