package com.example.mixtotrackmobile.ui.docente.talleres

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.TallerDocenteResponse
import com.example.mixtotrackmobile.databinding.FragmentTalleresBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TalleresFragment : BaseFragment<FragmentTalleresBinding>() {

    private val viewModel: TalleresViewModel by viewModels()
    private lateinit var adapter: TalleresDocenteAdapter
    private var listaTalleres: List<TallerDocenteResponse> = emptyList()

    override fun inflateBinding() = FragmentTalleresBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarTalleres()
    }

    private fun setupRecyclerView() {
        adapter = TalleresDocenteAdapter(
            onVerSolicitudesClick = { taller ->
                Toast.makeText(requireContext(),
                    "Solicitudes de ${taller.nombre}",
                    Toast.LENGTH_SHORT).show()
            },
            onCambiarEstadoClick = { taller ->
                mostrarDialogoCambiarEstado(taller)
            },
            onEliminarClick = { taller ->
                mostrarDialogoEliminar(taller)
            }
        )

        binding.rvTalleres.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TalleresFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnCrearTaller.setOnClickListener {
            mostrarDialogoCrearTaller()
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvTalleres.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.talleres.observe(viewLifecycleOwner) { lista ->
            listaTalleres = lista

            if (lista.isEmpty()) {
                binding.rvTalleres.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvTalleres.visibility = View.VISIBLE
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

    private fun mostrarDialogoCrearTaller() {
        val dialog = CrearTallerDialog()
        dialog.setOnTallerCreado {
            cargarTalleres() // Recargar lista después de crear
        }
        dialog.show(childFragmentManager, "crear_taller")
    }

    private fun mostrarDialogoCambiarEstado(taller: TallerDocenteResponse) {
        val estados = arrayOf("activo", "inactivo", "completo")
        val estadoSeleccionado = when (taller.estado) {
            "activo" -> 0
            "inactivo" -> 1
            "completo" -> 2
            else -> 0
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Cambiar Estado")
            .setMessage("Selecciona el nuevo estado para '${taller.nombre}'")
            .setSingleChoiceItems(estados, estadoSeleccionado) { dialog, which ->
                val nuevoEstado = estados[which]
                viewModel.cambiarEstadoTaller(taller.id, nuevoEstado)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(taller: TallerDocenteResponse) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Taller")
            .setMessage("¿Estás seguro de eliminar '${taller.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarTaller(taller.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cargarTalleres() {
        viewModel.cargarTalleres()
    }
}