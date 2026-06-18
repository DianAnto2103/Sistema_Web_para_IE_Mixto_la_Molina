package com.example.mixtotrackmobile.ui.common.perfil

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.entities.PerfilData
import com.example.mixtotrackmobile.databinding.FragmentPerfilBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import com.example.mixtotrackmobile.utils.UserRole
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerfilFragment : BaseFragment<FragmentPerfilBinding>() {

    private val viewModel: PerfilViewModel by viewModels()

    override fun inflateBinding() = FragmentPerfilBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupClickListeners()
        cargarPerfil()
    }

    private fun setupObservers() {
        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnActualizar.isEnabled = !loading
        }

        // Datos del perfil
        viewModel.perfilData.observe(viewLifecycleOwner) { perfil ->
            perfil?.let {
                // Datos comunes
                binding.tvNombreCompleto.text = "${it.nombre} ${it.apellido}"
                binding.tvRol.text = when (it.rol) {
                    "ALUMNO" -> "Alumno"
                    "DOCENTE" -> "Docente"
                    else -> it.rol
                }
                binding.tvEmail.text = it.email
                binding.etNombres.setText(it.nombre)
                binding.etApellidos.setText(it.apellido)
                binding.etEmail.setText(it.email)

                // Mostrar sección según rol
                when (it.rol) {
                    "ALUMNO" -> mostrarSeccionAlumno(it)
                    "DOCENTE" -> mostrarSeccionDocente(it)
                    else -> {
                        binding.layoutAlumno.visibility = View.GONE
                        binding.layoutDocente.visibility = View.GONE
                    }
                }
            }
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarSeccionAlumno(perfil: PerfilData) {
        binding.layoutAlumno.visibility = View.VISIBLE
        binding.layoutDocente.visibility = View.GONE

        binding.etGrado.setText(perfil.grado ?: "No asignado")
        binding.etSeccion.setText(perfil.seccion ?: "No asignada")
        binding.etPromedio.setText(perfil.promedioGeneral?.toString() ?: "Sin promedio")
    }

    private fun mostrarSeccionDocente(perfil: PerfilData) {
        binding.layoutAlumno.visibility = View.GONE
        binding.layoutDocente.visibility = View.VISIBLE

        binding.etDepartamento.setText(perfil.departamento ?: "No asignado")
        binding.etEspecialidad.setText(perfil.especialidad ?: "No asignada")
        binding.etExperiencia.setText(perfil.añosExperiencia?.toString() ?: "0 años")
    }

    private fun setupClickListeners() {
        binding.btnActualizar.setOnClickListener {
            cargarPerfil()
        }
    }

    private fun cargarPerfil() {
        viewModel.cargarPerfil()
    }
}