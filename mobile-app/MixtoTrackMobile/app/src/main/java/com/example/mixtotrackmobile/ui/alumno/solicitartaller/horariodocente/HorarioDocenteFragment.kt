package com.example.mixtotrackmobile.ui.alumno.solicitartaller.horariodocente

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.HorarioResponse
import com.example.mixtotrackmobile.databinding.FragmentHorarioDocenteBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HorarioDocenteFragment : BaseFragment<FragmentHorarioDocenteBinding>() {

    private val viewModel: HorarioDocenteViewModel by viewModels()
    private lateinit var adapter: HorarioDocenteAdapter

    override fun inflateBinding() = FragmentHorarioDocenteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarHorarios()
    }

    private fun setupRecyclerView() {
        adapter = HorarioDocenteAdapter { horario ->
            mostrarConfirmacion(horario)
        }

        binding.rvHorarios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HorarioDocenteFragment.adapter
        }
    }

    private fun setupListeners() {
        // Botón de retroceso
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Filtro de búsqueda
        binding.etBuscar.addTextChangedListener(
            object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.filtrarHorarios(s.toString())
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            }
        )
    }

    private fun setupObservers() {
        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvHorarios.visibility = if (loading) View.GONE else View.VISIBLE
        }

        // Lista de horarios
        viewModel.horariosFiltrados.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvHorarios.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvHorarios.visibility = View.VISIBLE
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
    }

    private fun mostrarConfirmacion(horario: HorarioResponse) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar Horario")
            .setMessage("¿Deseas seleccionar este horario para el taller?\n\n" +
                    "Docente: ${horario.docente}\n" +
                    "Horario: ${horario.dia} ${horario.horaInicio} - ${horario.horaFin}")
            .setPositiveButton("Seleccionar") { _, _ ->
                // Aquí puedes navegar de vuelta o enviar el horario seleccionado
                Toast.makeText(requireContext(), "✅ Horario seleccionado", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarHorarios() {
        viewModel.cargarHorarios()
    }
}