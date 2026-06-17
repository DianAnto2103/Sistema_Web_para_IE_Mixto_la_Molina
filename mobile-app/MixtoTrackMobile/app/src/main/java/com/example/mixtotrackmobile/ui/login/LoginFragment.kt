package com.example.mixtotrackmobile.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentLoginBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import com.example.mixtotrackmobile.utils.UserRole
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun inflateBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRoleSelection()
        setupListeners()
        setupObservers()
        setupDefaultCredentials()
    }

    private fun setupRoleSelection() {
        selectRole(UserRole.ALUMNO)

        binding.cardRoleStudent.setOnClickListener {
            selectRole(UserRole.ALUMNO)
            binding.etEmail.setText("alumno@mixto.edu.pe")
            binding.etPassword.setText("123456")
            binding.tilEmail.error = null
            binding.tilPassword.error = null
        }

        binding.cardRoleTeacher.setOnClickListener {
            selectRole(UserRole.DOCENTE)
            binding.etEmail.setText("docente@mixto.edu.pe")
            binding.etPassword.setText("123456")
            binding.tilEmail.error = null
            binding.tilPassword.error = null
        }
    }

    private fun selectRole(rol: UserRole) {
        when (rol) {
            UserRole.ALUMNO -> {
                binding.cardRoleStudent.strokeWidth = 4
                binding.cardRoleTeacher.strokeWidth = 1
                binding.viewStudentSelected.visibility = View.VISIBLE
                binding.viewTeacherSelected.visibility = View.GONE
            }
            UserRole.DOCENTE -> {
                binding.cardRoleStudent.strokeWidth = 1
                binding.cardRoleTeacher.strokeWidth = 4
                binding.viewStudentSelected.visibility = View.GONE
                binding.viewTeacherSelected.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            realizarLogin()
        }

        binding.tvHelp.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Contacta al administrador del colegio",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.tilEmail.error = null
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.tilPassword.error = null
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LoginUiState.Idle -> {}
                    is LoginUiState.Loading -> mostrarLoading(true)
                    is LoginUiState.Success -> {
                        mostrarLoading(false)
                        mostrarExito(state.rol)
                    }
                    is LoginUiState.Error -> {
                        mostrarLoading(false)
                        mostrarError(state.message)
                    }
                }
            }
        }
    }

    private fun setupDefaultCredentials() {
        binding.etEmail.setText("")
        binding.etPassword.setText("")
        selectRole(UserRole.ALUMNO)
    }

    private fun realizarLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val rol = obtenerRolSeleccionado()

        if (!validarCampos(email, password)) {
            return
        }

        viewModel.login(email, password, rol)
    }

    private fun validarCampos(email: String, password: String): Boolean {
        var esValido = true

        if (email.isEmpty()) {
            binding.tilEmail.error = "Ingresa tu correo electrónico"
            esValido = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Ingresa tu contraseña"
            esValido = false
        }

        return esValido
    }

    private fun obtenerRolSeleccionado(): UserRole {
        return if (binding.viewStudentSelected.visibility == View.VISIBLE) {
            UserRole.ALUMNO
        } else {
            UserRole.DOCENTE
        }
    }

    private fun navegarSegunRol(rol: UserRole) {
        try {
            when (rol) {
                UserRole.ALUMNO -> {
                    findNavController().navigate(R.id.alumno_navigation)
                }
                UserRole.DOCENTE -> {
                    findNavController().navigate(R.id.docente_navigation)
                }
            }
            viewModel.guardarRol(rol)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al navegar: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun mostrarLoading(mostrar: Boolean) {
        binding.btnLogin.isEnabled = !mostrar
        binding.btnLogin.visibility = if (mostrar) View.GONE else View.VISIBLE
        binding.progressBar.visibility = if (mostrar) View.VISIBLE else View.GONE
        binding.cardRoleStudent.isEnabled = !mostrar
        binding.cardRoleTeacher.isEnabled = !mostrar
    }

    private fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), "$mensaje", Toast.LENGTH_SHORT).show()
    }

    private fun mostrarExito(rol: UserRole) {
        val mensaje = when (rol) {
            UserRole.ALUMNO -> "¡Bienvenido Alumno!"
            UserRole.DOCENTE -> "¡Bienvenido Docente!"
        }
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()

        binding.root.postDelayed({
            navegarSegunRol(rol)
        }, 500)
    }
}