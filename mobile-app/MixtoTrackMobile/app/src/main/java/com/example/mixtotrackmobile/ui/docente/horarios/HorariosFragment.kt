package com.example.mixtotrackmobile.ui.docente.horarios

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentHorariosBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HorariosFragment : BaseFragment<FragmentHorariosBinding>() {

    private val viewModel: HorariosViewModel by viewModels()
    private lateinit var adapter: HorariosAdapter

    override fun inflateBinding() = FragmentHorariosBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarHorarios()
    }

    private fun setupRecyclerView() {
        adapter = HorariosAdapter { horario, disponible ->
            viewModel.actualizarDisponibilidad(horario.id, disponible)
        }

        binding.rvHorarios.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HorariosFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvHorarios.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.horarios.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvHorarios.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvHorarios.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
            }
        }

        viewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarHorarios() {
        viewModel.cargarHorarios()
    }
}