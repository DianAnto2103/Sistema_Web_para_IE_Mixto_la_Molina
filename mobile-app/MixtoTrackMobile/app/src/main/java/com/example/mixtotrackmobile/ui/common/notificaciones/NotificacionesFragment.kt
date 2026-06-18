package com.example.mixtotrackmobile.ui.common.notificaciones

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentNotificacionesBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificacionesFragment : BaseFragment<FragmentNotificacionesBinding>() {

    private val viewModel: NotificacionesViewModel by viewModels()
    private lateinit var adapter: NotificacionesAdapter

    override fun inflateBinding() = FragmentNotificacionesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarNotificaciones()
    }

    private fun setupRecyclerView() {
        adapter = NotificacionesAdapter { notificacion ->
            viewModel.marcarComoLeida(notificacion.id)
        }

        binding.rvNotificaciones.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificacionesFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.tvMarcarTodas.setOnClickListener {
            viewModel.marcarTodasComoLeidas()
        }
    }

    private fun setupObservers() {
        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvNotificaciones.visibility = if (loading) View.GONE else View.VISIBLE
        }

        // Lista de notificaciones
        viewModel.notificaciones.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvNotificaciones.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvNotificaciones.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
            }
        }

        // Contador de no leídas
        viewModel.contadorNoLeidas.observe(viewLifecycleOwner) { count ->
            binding.tvContador.text = "$count notificaciones sin leer"
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }

        // Actualizar después de marcar como leída
        viewModel.actualizarLista.observe(viewLifecycleOwner) {
            if (it) {
                cargarNotificaciones()
            }
        }
    }

    private fun cargarNotificaciones() {
        viewModel.cargarNotificaciones()
    }
}