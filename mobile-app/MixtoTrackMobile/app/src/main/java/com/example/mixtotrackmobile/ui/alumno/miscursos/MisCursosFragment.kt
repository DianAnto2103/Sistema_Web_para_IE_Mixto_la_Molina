package com.example.mixtotrackmobile.ui.alumno.miscursos

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentMisCursosBinding
import com.example.mixtotrackmobile.ui.alumno.mis_cursos.MisCursosAdapter
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class MisCursosFragment : BaseFragment<FragmentMisCursosBinding>() {

    private val viewModel: MisCursosViewModel by viewModels()
    private lateinit var adapter: MisCursosAdapter
    private val df = DecimalFormat("#.#")

    override fun inflateBinding() = FragmentMisCursosBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarCursos()
    }

    private fun setupRecyclerView() {
        adapter = MisCursosAdapter { curso ->
            val bundle = Bundle().apply {
                putInt("curso_id", curso.id)
                putString("curso_nombre", curso.nombre)
            }
            try {
                findNavController().navigate(
                    R.id.action_misCursos_to_detalleCurso,
                    bundle
                )
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvCursos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MisCursosFragment.adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupObservers() {
        // Estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.rvCursos.visibility = if (loading) View.GONE else View.VISIBLE
        }

        // Lista de cursos
        viewModel.cursos.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvCursos.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvCursos.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
            }
        }

        // Promedio general
        viewModel.promedioGeneral.observe(viewLifecycleOwner) { promedio ->
            binding.tvPromedioGeneral.text = "Promedio: ${df.format(promedio)}"
        }

        // Errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarCursos() {
        viewModel.cargarCursos()
    }
}