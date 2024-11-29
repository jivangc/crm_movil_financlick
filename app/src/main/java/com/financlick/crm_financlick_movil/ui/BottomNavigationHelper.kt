package com.financlick.crm_financlick_movil.ui
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.financlick.crm_financlick_movil.R

class BottomNavigationHelper(private val context: Context) {

    fun setupBottomNavigation(bottomNavigationLayout: LinearLayout) {
        val btnMenu = bottomNavigationLayout.findViewById<ImageButton>(R.id.btnMenu)
        val btnPlanificacion = bottomNavigationLayout.findViewById<ImageButton>(R.id.btnPlanificacion)
        val btnVentas = bottomNavigationLayout.findViewById<ImageButton>(R.id.btnVentas)
        val btnFinanzas = bottomNavigationLayout.findViewById<ImageButton>(R.id.btnFinanzas)

        btnMenu.setOnClickListener { navigateToActivity(HomeActivity::class.java) }
        btnPlanificacion.setOnClickListener { navigateToActivity(PlanificacionesPrincipal::class.java) }
        btnVentas.setOnClickListener { navigateToActivity(VentasActivity::class.java) }
        btnFinanzas.setOnClickListener { navigateToActivity(FinanzasActivity::class.java) }
    }

    private fun navigateToActivity(activityClass: Class<out AppCompatActivity>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
    }
}