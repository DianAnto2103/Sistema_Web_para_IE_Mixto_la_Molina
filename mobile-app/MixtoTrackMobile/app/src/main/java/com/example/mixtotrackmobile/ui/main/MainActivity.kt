package com.example.mixtotrackmobile.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.ActivityMainBinding
import com.example.mixtotrackmobile.utils.SessionManager
import com.example.mixtotrackmobile.utils.UserRole
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inflar el layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Configurar navegación
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 3. Configurar ActionBar (Toolbar) - Opcional
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.menuAlumnoFragment,
                R.id.menuDocenteFragment,
                R.id.perfilFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 4. Verificar si ya hay sesión (opcional)
        verificarSesion()
    }

    private fun verificarSesion() {
        val rol = sessionManager.getRol()
        val token = sessionManager.getToken()

        if (token != null && rol != null) {
            // Si ya hay sesión, navegar directamente al menú correspondiente
            when (rol) {
                UserRole.ALUMNO -> {
                    navController.navigate(R.id.alumno_navigation)
                }
                UserRole.DOCENTE -> {
                    navController.navigate(R.id.docente_navigation)
                }
            }
        }
    }

    // 5. Manejar el botón "atrás"
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}