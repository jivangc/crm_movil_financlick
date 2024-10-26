package com.financlick.crm_financlick_movil.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financlick.crm_financlick_movil.R
import com.financlick.crm_financlick_movil.adapters.CardCampaniaAdapter
import com.financlick.crm_financlick_movil.items.CardCampaniaItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Date

class CampanasActivity : AppCompatActivity() {
    private lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_campanas)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = CardCampaniaAdapter(emptyList())
        recyclerView.adapter = adapter

        floatingButton = findViewById(R.id.addCampania)
        floatingButton.setOnClickListener {
            val intent = Intent(this, CampaniaFormActivity::class.java)
            startActivity(intent)
        }

        val campaniasTest = listOf(
            CardCampaniaItem(
                idCampania = 1,
                tipoCampania = "Publicitaria",
                titulo = "Descubre nuestros productos",
                descripcion = "Te presentamos nuestros productos",
                dominioCampania = "www.financlick.com",
                fechaInicio = Date(),
                fechaFin = null,
                contenido = "Aqui va el contenido de la campania",
                adjunto = "",
                status = "En progreso"
            ),
            CardCampaniaItem(
                idCampania = 2,
                tipoCampania = "Informativa",
                titulo = "Novedades en la app",
                descripcion = "Nuevas funcionalidades",
                dominioCampania = "www.financlick.com/novedades",
                fechaInicio = Date(),
                fechaFin = null,
                contenido = "Aqui va el contenido de la campania",
                adjunto = "",
                status = "En progreso"
            ),
            CardCampaniaItem(
                idCampania = 3,
                tipoCampania = "Publicitaria",
                titulo = "Encuentra el mejor plan para ti",
                descripcion = "Te presentamos nuestros planes",
                dominioCampania = "www.financlick.com/planes",
                fechaInicio = Date(),
                fechaFin = null,
                contenido = "Aqui va el contenido de la campania",
                adjunto = "",
                status = "En progreso"
            ),
            CardCampaniaItem(
                idCampania = 4,
                tipoCampania = "Informativa",
                titulo = "Nuevas funcionalidades en la app",
                descripcion = "Nuevas funcionalidades",
                dominioCampania = "www.financlick.com/novedades",
                fechaInicio = Date(),
                fechaFin = null,
                contenido = "Aqui va el contenido de la campania",
                adjunto = "",
                status = "En progreso"
            ),
            CardCampaniaItem(
                idCampania = 5,
                tipoCampania = "Publicitaria",
                titulo = "Encuentra el mejor plan para ti",
                descripcion = "Te presentamos nuestros planes",
                dominioCampania = "www.financlick.com/planes",
                fechaInicio = Date(),
                fechaFin = null,
                contenido = "Aqui va el contenido de la campania",
                adjunto = "",
                status = "En progreso"
            )
        )

        adapter.updateItems(campaniasTest)
    }
}