package com.example.mixtotrackmobile.ui.alumno.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentMenuAlumnoBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuAlumnoFragment : BaseFragment<FragmentMenuAlumnoBinding>() {

    private val viewModel: MenuAlumnoViewModel by viewModels()

    override fun inflateBinding() = FragmentMenuAlumnoBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.nombreAlumno.observe(viewLifecycleOwner) { nombre ->
            binding.tvNombreAlumno.text = "Bienvenido, $nombre"
        }

        viewModel.gradoAlumno.observe(viewLifecycleOwner) { grado ->
            binding.tvGradoAlumno.text = grado
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.btnLogout.isEnabled = !loading
        }
    }

    private fun setupClickListeners() {
        // Mi Perfil
        binding.cardPerfil.setOnClickListener {
            Toast.makeText(requireContext(), "Mi Perfil - Próximamente", Toast.LENGTH_SHORT).show()
            // findNavController().navigate(R.id.action_menuAlumno_to_perfil)
        }

        // Mis Cursos
        binding.cardMisCursos.setOnClickListener {
            Toast.makeText(requireContext(), "Mis Cursos - Próximamente", Toast.LENGTH_SHORT).show()
            // findNavController().navigate(R.id.action_menuAlumno_to_misCursos)
        }

        // Solicitar Taller
        binding.cardSolicitarTaller.setOnClickListener {
            Toast.makeText(requireContext(), "Solicitar Taller - Próximamente", Toast.LENGTH_SHORT).show()
            // findNavController().navigate(R.id.action_menuAlumno_to_solicitarTaller)
        }

        // Grupo de Estudio
        binding.cardGrupoEstudio.setOnClickListener {
            Toast.makeText(requireContext(), "Grupo de Estudio - Próximamente", Toast.LENGTH_SHORT).show()
            // findNavController().navigate(R.id.action_menuAlumno_to_grupoEstudio)
        }

        // Notificaciones
        binding.ivNotificaciones.setOnClickListener {
            Toast.makeText(requireContext(), "Notificaciones - Próximamente", Toast.LENGTH_SHORT).show()
        }

        // Cerrar Sesión
        binding.btnLogout.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.cerrarSesion()
                // Navegar al Login
                findNavController().navigate(R.id.action_menuAlumno_to_login)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}