package com.example.mixtotrackmobile.ui.alumno.mis_talleres

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentMisTalleresBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MisTalleresFragment : BaseFragment<FragmentMisTalleresBinding>() {

    private val viewModel: MisTalleresViewModel by viewModels()
    private lateinit var adapter: MisTalleresAdapter
    private var filtroActual: String = "todos"

    override fun inflateBinding() = FragmentMisTalleresBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarSolicitudes()
    }

    private fun setupRecyclerView() {
        adapter = MisTalleresAdapter { taller ->
            Toast.makeText(requireContext(),
                "${taller.tallerNombre} - ${taller.estado}",
                Toast.LENGTH_SHORT).show()
        }

        binding.rvTalleres.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MisTalleresFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Filtros
        binding.chipTodos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) aplicarFiltro("todos")
        }
        binding.chipPendiente.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) aplicarFiltro("pendiente")
        }
        binding.chipAprobado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) aplicarFiltro("aprobado")
        }
        binding.chipRechazado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) aplicarFiltro("rechazado")
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvTalleres.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.solicitudes.observe(viewLifecycleOwner) { lista ->
            binding.tvContador.text = "${lista.size} talleres"

            if (lista.isEmpty()) {
                binding.rvTalleres.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvTalleres.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista, filtroActual)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun aplicarFiltro(filtro: String) {
        filtroActual = filtro
        viewModel.solicitudes.value?.let {
            adapter.setData(it, filtro)
        }
    }

    private fun cargarSolicitudes() {
        viewModel.cargarSolicitudes()
    }
}