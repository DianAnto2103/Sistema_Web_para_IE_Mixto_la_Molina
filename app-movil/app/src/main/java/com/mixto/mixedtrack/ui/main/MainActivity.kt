package com.mixto.mixedtrack.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mixto.mixedtrack.R
import com.mixto.mixedtrack.databinding.ActivityMainBinding
import com.mixto.mixedtrack.ui.auth.LoginActivity
import com.mixto.mixedtrack.ui.fragments.MisCalificacionesFragment
import com.mixto.mixedtrack.ui.fragments.SolicitarTallerFragment
import com.mixto.mixedtrack.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validateSession()
        setupNavigation()
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }

    private fun validateSession() {
        if (viewModel.getSavedToken().isNullOrEmpty()) {
            navigateToLogin()
            return
        }
        openFragment(MisCalificacionesFragment())
        binding.bottomNavigation.selectedItemId = R.id.navigation_grades
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_grades -> {
                    openFragment(MisCalificacionesFragment())
                    binding.toolbarTitle.text = getString(R.string.menu_grades)
                    true
                }
                R.id.navigation_workshops -> {
                    openFragment(SolicitarTallerFragment())
                    binding.toolbarTitle.text = getString(R.string.menu_workshops)
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
