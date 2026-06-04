package com.example.mixtotrackmobile.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mixtotrackmobile.R

class MainActivity : AppCompatActivity() {

    private lateinit var tvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInfo = findViewById(R.id.tvInfo)

        val rol = intent.getStringExtra("rol") ?: "desconocido"
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("access_token", "no token")

        tvInfo.text = """
            LOGIN EXITOSO
            
            Rol: $rol
            Token: ${token?.take(50)}...
            
            (Prueba superada)
        """.trimIndent()
    }
}

