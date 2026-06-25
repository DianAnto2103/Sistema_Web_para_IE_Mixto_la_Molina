package com.example.mixtotrackmobile.ui.docente.cursos.busquedaalumno

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentBusquedaAlumnoBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import com.example.mixtotrackmobile.ui.docente.cursos.busqueda_alumno.BusquedaAlumnoViewModel
import com.example.mixtotrackmobile.ui.docente.cursos.busquedaalumno.AlumnoBusquedaAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BusquedaAlumnoFragment : BaseFragment<FragmentBusquedaAlumnoBinding>() {

    private val viewModel: BusquedaAlumnoViewModel by viewModels()
    private lateinit var adapter: AlumnoBusquedaAdapter
    private var searchJob: Job? = null

    override fun inflateBinding() = FragmentBusquedaAlumnoBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = AlumnoBusquedaAdapter { alumno ->
            val bundle = Bundle().apply {
                putInt("alumno_id", alumno.id)
                putString("alumno_nombre", "${alumno.nombre} ${alumno.apellido}")
                putInt("curso_id", 1) // Aquí deberías obtener el curso seleccionado
                putString("curso_nombre", "Matemáticas")
                putString("grado", alumno.grado)
                putString("seccion", alumno.seccion)
            }
            findNavController().navigate(
                R.id.action_busquedaAlumno_to_registrarCalificacion,
                bundle
            )
        }

        binding.rvAlumnos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BusquedaAlumnoFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.etBuscar.addTextChangedListener(
            object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    buscarAlumnos(s.toString())
                }
                override fun afterTextChanged(s: android.text.Editable?) {}
            }
        )
    }

    private fun buscarAlumnos(query: String) {
        // Debounce para no hacer muchas peticiones
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            delay(300) // Esperar a que el usuario deje de escribir
            viewModel.buscarAlumnos(query)
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.alumnos.observe(viewLifecycleOwner) { lista ->
            binding.tvResultados.text = "${lista.size} resultados"

            if (lista.isEmpty()) {
                binding.rvAlumnos.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvAlumnos.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }
}