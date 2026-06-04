package com.example.mixtotrackmobile.ui.login

<<<<<<< HEAD
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.model.LoginRequest
import com.example.mixtotrackmobile.data.network.RetrofitClient
import com.example.mixtotrackmobile.ui.main.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var cardRoleStudent: MaterialCardView
    private lateinit var cardRoleTeacher: MaterialCardView
    private lateinit var sharedPref: SharedPreferences

    private var rolSeleccionado: String = "alumno"
=======
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
>>>>>>> 5ee7b4cb24a0ee6b4da087ec9e3d0b3cea0e2774

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

<<<<<<< HEAD
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        cardRoleStudent = findViewById(R.id.cardRoleStudent)
        cardRoleTeacher = findViewById(R.id.cardRoleTeacher)

        sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Verificar sesión activa
        val token = sharedPref.getString("access_token", null)
        val rol = sharedPref.getString("rol", null)
        if (!token.isNullOrEmpty() && !rol.isNullOrEmpty()) {
            irMainActivity(rol)
            return
        }

        // Selección visual del rol
        seleccionarRol("alumno")

        cardRoleStudent.setOnClickListener {
            seleccionarRol("alumno")
        }

        cardRoleTeacher.setOnClickListener {
            seleccionarRol("docente")
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                hacerLogin(username, password)
            } else {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
=======
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
>>>>>>> 5ee7b4cb24a0ee6b4da087ec9e3d0b3cea0e2774
            }
        }
    }

    private fun seleccionarRol(rol: String) {
        rolSeleccionado = rol
        val strokeColor = getColor(R.color.primary)

        if (rol == "alumno") {
            cardRoleStudent.strokeWidth = 4
            cardRoleStudent.strokeColor = strokeColor
            cardRoleTeacher.strokeWidth = 1
        } else {
            cardRoleTeacher.strokeWidth = 4
            cardRoleTeacher.strokeColor = strokeColor
            cardRoleStudent.strokeWidth = 1
        }
    }

    private fun hacerLogin(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.login(LoginRequest(username, password))

                // Guardar token y rol
                sharedPref.edit().putString("access_token", response.access).apply()
                sharedPref.edit().putString("rol", rolSeleccionado).apply()

                Toast.makeText(this@LoginActivity, "Bienvenido $username", Toast.LENGTH_SHORT).show()

                irMainActivity(rolSeleccionado)

            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun irMainActivity(rol: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("rol", rol)
        startActivity(intent)
        finish()
    }
}