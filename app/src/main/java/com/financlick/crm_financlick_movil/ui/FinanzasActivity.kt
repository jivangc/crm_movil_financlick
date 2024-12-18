package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.api.RetrofitClient
import com.financlick.crm_financlick_movil.models.AcumuladoAnualModel
import com.financlick.crm_financlick_movil.models.TotalesMensualesModel
import com.github.mikephil.charting.charts.BarChart // Importa el BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.BarData // Importa el BarData
import com.github.mikephil.charting.data.BarDataSet // Importa el BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinanzasActivity : AppCompatActivity() {

    private lateinit var chartTotalesMensuales: BarChart // Usa BarChart
    private lateinit var chartAcumuladoAnual: PieChart
    private lateinit var pagosButton: Button
    private lateinit var egresosButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finanzas)


        val bottomNavigationLayout = findViewById<LinearLayout>(R.id.bottomNavigation)
        val bottomNavigationHelper = BottomNavigationHelper(this)
        bottomNavigationHelper.setupBottomNavigation(bottomNavigationLayout)

        // Vinculación de vistas
        chartTotalesMensuales = findViewById(R.id.chartTotalesMensuales) // Asegúrate de que el ID corresponde a un BarChart
        chartAcumuladoAnual = findViewById(R.id.chartAcumuladoAnual)
        pagosButton = findViewById(R.id.pagosButton)
        egresosButton = findViewById(R.id.egresosButton)

        // Botón para navegar a la vista de pagos
        pagosButton.setOnClickListener {
            val intent = Intent(this, PagosActivity::class.java)
            startActivity(intent)
        }

        egresosButton.setOnClickListener {
            val intent = Intent(this, InfoEgresosActivity::class.java)
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
                        // Crear entradas para las barras
                        val ingresosEntries = ArrayList<BarEntry>()
                        val egresosEntries = ArrayList<BarEntry>()

                        data.forEachIndexed { index, item ->
                            ingresosEntries.add(BarEntry(index.toFloat(), item.totalIngresos.toFloat()))
                            egresosEntries.add(BarEntry(index.toFloat(), item.totalEgresos.toFloat()))
                        }

                        // Crear DataSets para las barras
                        val ingresosDataSet = BarDataSet(ingresosEntries, "Ingresos")
                        val egresosDataSet = BarDataSet(egresosEntries, "Egresos")

                        // Personalizar estilo de las barras
                        ingresosDataSet.color = ContextCompat.getColor(this@FinanzasActivity, R.color.purple_200)
                        egresosDataSet.color = ContextCompat.getColor(this@FinanzasActivity, R.color.red)

                        // Configurar agrupación de barras
                        val groupSpace = 0.3f
                        val barSpace = 0.05f
                        val barWidth = 0.3f

                        // Crear BarData con ambos conjuntos de datos
                        val barData = BarData(ingresosDataSet, egresosDataSet)
                        barData.barWidth = barWidth

                        // Configurar el gráfico
                        chartTotalesMensuales.data = barData
                        chartTotalesMensuales.groupBars(0f, groupSpace, barSpace)

                        // Configurar el eje X
                        val xAxis = chartTotalesMensuales.xAxis
                        xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.mes.toString() })
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.granularity = 1f
                        xAxis.setCenterAxisLabels(true)

                        // Ajustar los límites del eje X
                        chartTotalesMensuales.xAxis.axisMinimum = 0f
                        chartTotalesMensuales.xAxis.axisMaximum = data.size.toFloat()

                        // Otras configuraciones del gráfico
                        chartTotalesMensuales.setFitBars(true)
                        chartTotalesMensuales.description.isEnabled = false
                        chartTotalesMensuales.legend.isEnabled = true

                        // Refrescar la gráfica
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

                        // Sumar los valores de ingresos y egresos
                        val ingresosSum = data.sumOf { it.ingresosAcumulados.toDouble() }
                        val egresosSum = data.sumOf { it.egresosAcumulados.toDouble() }

                        // Crear entradas para el gráfico circular (PieChart)
                        val pieEntries = listOf(
                            PieEntry(ingresosSum.toFloat(), "Ingresos"),
                            PieEntry(egresosSum.toFloat(), "Egresos")
                        )

                        // Crear y personalizar el DataSet del gráfico circular
                        val dataSet = PieDataSet(pieEntries, "Acumulado Anual")
                        dataSet.colors = listOf(
                            ColorTemplate.COLORFUL_COLORS[0],
                            ColorTemplate.COLORFUL_COLORS[1]
                        )

                        // Asignar los datos y actualizar la gráfica
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
