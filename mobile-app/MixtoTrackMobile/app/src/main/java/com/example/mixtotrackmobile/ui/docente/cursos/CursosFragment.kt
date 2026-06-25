package com.example.mixtotrackmobile.ui.docente.cursos

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentCursosBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CursosFragment : BaseFragment<FragmentCursosBinding>() {

    private val viewModel: CursosViewModel by viewModels()
    private lateinit var adapter: CursosAdapter

    override fun inflateBinding() = FragmentCursosBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarCursos()
    }

    private fun setupRecyclerView() {
        adapter = CursosAdapter(
            onVerAlumnosClick = { curso ->
                val bundle = Bundle().apply {
                    putInt("curso_id", curso.id)
                    putString("curso_nombre", curso.nombre)
                }
                findNavController().navigate(
                    R.id.action_cursos_to_detalleCursoDocente,
                    bundle
                )
            },
            onCalificacionesClick = { curso ->
                // Navegar a registrar calificaciones con el curso seleccionado
                val bundle = Bundle().apply {
                    putInt("curso_id", curso.id)
                    putString("curso_nombre", curso.nombre)
                }
                findNavController().navigate(
                    R.id.action_cursos_to_registrarCalificacion,
                    bundle
                )
            }
        )

        binding.rvCursos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CursosFragment.adapter
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
            binding.rvCursos.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewModel.cursos.observe(viewLifecycleOwner) { lista ->
            if (lista.isEmpty()) {
                binding.rvCursos.visibility = View.GONE
                binding.layoutVacio.visibility = View.VISIBLE
            } else {
                binding.rvCursos.visibility = View.VISIBLE
                binding.layoutVacio.visibility = View.GONE
                adapter.setData(lista)
                binding.tvTotalCursos.text = "${lista.size} cursos"
            }
        }

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