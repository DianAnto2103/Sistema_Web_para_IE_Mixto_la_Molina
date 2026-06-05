package com.mixto.mixedtrack.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mixto.mixedtrack.databinding.ActivityLoginBinding
import com.mixto.mixedtrack.ui.main.MainActivity
import com.mixto.mixedtrack.ui.viewmodel.MainViewModel
import com.mixto.mixedtrack.util.Resource
import kotlinx.coroutines.flow.collect

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLoginState()
        checkExistingSession()

        binding.buttonLogin.setOnClickListener {
            binding.inputLayoutUsername.error = null
            binding.inputLayoutPassword.error = null
            viewModel.login(
                binding.editTextUsername.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }
    }

    private fun checkExistingSession() {
        val token = viewModel.getSavedToken()
        if (!token.isNullOrEmpty()) {
            navigateToMain()
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { state ->
                when (state) {
                    Resource.Idle -> {
                        binding.progressBar.hide()
                    }
                    Resource.Loading -> {
                        binding.progressBar.show()
                    }
                    is Resource.Success -> {
                        binding.progressBar.hide()
                        navigateToMain()
                    }
                    is Resource.Error -> {
                        binding.progressBar.hide()
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun android.view.View.show() {
        this.visibility = android.view.View.VISIBLE
    }

    private fun android.view.View.hide() {
        this.visibility = android.view.View.GONE
    }
}
