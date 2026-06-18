package com.example.mixtotrackmobile.ui.alumno.grupoestudio

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentGrupoEstudioBinding
import com.example.mixtotrackmobile.ui.alumno.grupo_estudio.CrearGrupoDialog
import com.example.mixtotrackmobile.ui.alumno.grupo_estudio.GrupoEstudioViewModel
import com.example.mixtotrackmobile.ui.alumno.grupo_estudio.GrupoEstudioAdapter
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GrupoEstudioFragment : BaseFragment<FragmentGrupoEstudioBinding>() {

    private val viewModel: GrupoEstudioViewModel by viewModels()
    private lateinit var adapter: GrupoEstudioAdapter

    override fun inflateBinding() = FragmentGrupoEstudioBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarGrupos()
    }

    private fun setupRecyclerView() {
        adapter = GrupoEstudioAdapter(
            onUnirseClick = { grupo -> viewModel.unirseAGrupo(grupo.id) },
            onSalirClick = { grupo -> viewModel.salirDelGrupo(grupo.id) },
            onEliminarClick = { grupo -> viewModel.eliminarGrupo(grupo.id) }
        )

        binding.rvGrupos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@GrupoEstudioFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnCrearGrupo.setOnClickListener {
            mostrarDialogoCrear()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvGrupos.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.grupos.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvGrupos.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvGrupos.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
            }
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                // Recargar después de una acción
                cargarGrupos()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarDialogoCrear() {
        val dialog = CrearGrupoDialog { nombre, curso, descripcion, cupos ->
            viewModel.crearGrupo(nombre, curso, descripcion, cupos)
        }
        dialog.show(childFragmentManager, "crear_grupo")
    }

    private fun cargarGrupos() {
        viewModel.cargarGrupos()
    }
}