package com.financlick.crm_financlick_movil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.financlick.crm_financlick_movil.ui.HomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var contrasenaInputLayout: TextInputLayout
    private lateinit var contrasenaInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Asegúrate de que el tema está aplicado antes de llamar a super.onCreate
        setContentView(R.layout.activity_main)

        // Inicializa las vistas
        contrasenaInputLayout = findViewById(R.id.passwordInputLayout)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        contrasenaInput = findViewById(R.id.passwordEditText)
        emailInput = findViewById(R.id.usernameEditText)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            login(emailInput.text.toString(), contrasenaInput.text.toString())
        }
    }

    private fun login(user: String, password: String) {
        val defaultEmail = "admin"
        val defaultPassword = "admin"

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (user == defaultEmail && password == defaultPassword) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }
}
