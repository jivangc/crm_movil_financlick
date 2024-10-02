package com.financlick.crm_financlick_movil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.financlick.crm_financlick_movil.ui.HomeActivity
import com.financlick.crm_financlick_movil.ui.theme.Crm_financlick_movilTheme
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    lateinit var contexto: Context
    lateinit var emailInputLayout: TextInputLayout
    lateinit var emailInput: TextInputEditText
    lateinit var contrasenaInputLayout: TextInputLayout
    lateinit var contrasenaInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        contrasenaInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        contrasenaInput = findViewById<TextInputEditText>(R.id.passwordEditText)
        emailInputLayout = findViewById<TextInputLayout>(R.id.emailInputLayout)
        emailInput = findViewById<TextInputEditText>(R.id.usernameEditText)


        findViewById<Button>(R.id.loginButton).setOnClickListener {
            login(emailInput.text.toString(), contrasenaInput.text.toString())
        }
    }

    fun login(user: String, password: String) {
        val defaultEmail = "admin"
        val defaultPassword = "admin"
        if (user == defaultEmail && password == defaultPassword) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(contexto, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }
}
