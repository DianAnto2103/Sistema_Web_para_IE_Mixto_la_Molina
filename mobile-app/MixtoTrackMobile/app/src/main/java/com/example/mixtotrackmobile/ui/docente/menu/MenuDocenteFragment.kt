package com.example.mixtotrackmobile.ui.docente.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentMenuDocenteBinding
import com.example.mixtotrackmobile.utils.SessionManager
import com.example.mixtotrackmobile.utils.UserRole
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuDocenteFragment : Fragment() {

    private var _binding: FragmentMenuDocenteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuDocenteViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuDocenteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cargarNombreDocente()
        viewModel.cargarCursosCount()
        setupNavigation()
        setupObservers()
    }

    private fun cargarNombreDocente() {
        val nombreCompleto = sessionManager.getNombreCompleto()
        val rol = sessionManager.getRol()

        if (rol == UserRole.DOCENTE) {
            binding.tvDocenteNombre.text = nombreCompleto
            binding.tvCargando.visibility = View.VISIBLE
        } else {
            binding.tvDocenteNombre.text = "Docente"
            binding.tvCargando.visibility = View.VISIBLE
        }
    }

    private fun setupNavigation() {
        // ===== Mis Cursos =====
        binding.cardCursos.setOnClickListener {
            findNavController().navigate(R.id.action_menuDocente_to_cursos)
        }


        binding.cardBuscar.setOnClickListener {
            findNavController().navigate(R.id.action_menuDocente_to_buscarAlumnos)
        }

        // ===== Registrar Calificaciones =====
        binding.cardCalificaciones.setOnClickListener {
            findNavController().navigate(R.id.action_menuDocente_to_registrarCalificacion)
        }

        // ===== Mis Horarios (RF13) =====
        binding.cardHorarios.setOnClickListener {
            findNavController().navigate(R.id.action_menuDocente_to_horarios)
        }

        // ===== Mi Perfil =====
        binding.cardPerfil.setOnClickListener {
            findNavController().navigate(R.id.action_menuDocente_to_perfil)
        }

        // ===== Talleres =====
        binding.cardTalleres.setOnClickListener {
            findNavController().navigate(R.id.action_menuDocente_to_talleres)
        }

        // ===== Cerrar Sesión =====
        binding.cardCerrarSesion.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }
    }

    private fun setupObservers() {
        viewModel.cursosCount.observe(viewLifecycleOwner) { count ->
            binding.tvCargando.visibility = View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.tvCargando.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.tvCargando.visibility = View.GONE
            }
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Está seguro de cerrar sesión?")
            .setPositiveButton("Cerrar Sesión") { _, _ ->
                sessionManager.cerrarSesion()
                irALogin()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun irALogin() {
        findNavController().navigate(R.id.action_menuDocente_to_login)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}