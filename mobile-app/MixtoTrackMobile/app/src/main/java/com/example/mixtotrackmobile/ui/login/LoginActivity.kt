package com.example.mixtotrackmobile.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.ui.alumno.MenuAlumnoActivity
import com.example.mixtotrackmobile.ui.docente.MenuDocenteActivity

class LoginActivity : AppCompatActivity() {

    private var rolSeleccionado = "Alumno"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val rbAlumno = findViewById<RadioButton>(R.id.rbAlumno)
        val rbDocente = findViewById<RadioButton>(R.id.rbDocente)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        rbAlumno.isChecked = true
        rbDocente.isChecked = false

        rbAlumno.setOnClickListener {
            rolSeleccionado = "Alumno"
            rbAlumno.isChecked = true
            rbDocente.isChecked = false

            rbAlumno.setBackgroundColor(android.graphics.Color.parseColor("#E8B84B"))
            rbAlumno.setTextColor(android.graphics.Color.parseColor("#0E1F5B"))

            rbDocente.setBackgroundColor(android.graphics.Color.parseColor("#334BA3"))
            rbDocente.setTextColor(android.graphics.Color.WHITE)
        }

        rbDocente.setOnClickListener {
            rolSeleccionado = "Docente"
            rbDocente.isChecked = true
            rbAlumno.isChecked = false

            rbDocente.setBackgroundColor(android.graphics.Color.parseColor("#E8B84B"))
            rbDocente.setTextColor(android.graphics.Color.parseColor("#0E1F5B"))

            rbAlumno.setBackgroundColor(android.graphics.Color.parseColor("#334BA3"))
            rbAlumno.setTextColor(android.graphics.Color.WHITE)
        }

        btnIngresar.setOnClickListener {
            if (rolSeleccionado == "Alumno") {
                startActivity(Intent(this, MenuAlumnoActivity::class.java))
            } else {
                startActivity(Intent(this, MenuDocenteActivity::class.java))
            }
        }
    }
}