package com.example.mixtotrackmobile.ui.alumno.solicitar_taller

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.TallerResponse
import com.example.mixtotrackmobile.databinding.FragmentSolicitarTallerBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolicitarTallerFragment : BaseFragment<FragmentSolicitarTallerBinding>() {

    private val viewModel: SolicitarTallerViewModel by viewModels()
    private lateinit var adapter: TalleresAdapter

    override fun inflateBinding() = FragmentSolicitarTallerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarTalleres()
    }

    private fun setupRecyclerView() {
        adapter = TalleresAdapter { taller ->
            mostrarDialogoSolicitud(taller)
        }

        binding.rvTalleres.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SolicitarTallerFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.tvMisTalleres.setOnClickListener {
            Toast.makeText(requireContext(), "Mis talleres - Próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvTalleres.visibility = if (loading) View.GONE else View.VISIBLE
        }

        // Lista de talleres
        viewModel.talleres.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvTalleres.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvTalleres.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
            }
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }

        // Mensaje de éxito en solicitud
        viewModel.mensajeExito.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), "✅ $it", Toast.LENGTH_LONG).show()
                // Recargar talleres para actualizar cupos
                cargarTalleres()
            }
        }
    }

    private fun mostrarDialogoSolicitud(taller: TallerResponse) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Solicitar Taller")
            .setMessage("¿Deseas solicitar el taller \"${taller.nombre}\"?")
            .setPositiveButton("Solicitar") { _, _ ->
                viewModel.solicitarTaller(taller.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarTalleres() {
        viewModel.cargarTalleres()
    }
}