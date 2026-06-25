package com.example.mixtotrackmobile.ui.docente.cursos.detallecurso

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentDetalleCursoDocenteBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import com.example.mixtotrackmobile.ui.docente.cursosdetalle_curso.DetalleCursoDocenteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetalleCursoDocenteFragment : BaseFragment<FragmentDetalleCursoDocenteBinding>() {

    private val viewModel: DetalleCursoDocenteViewModel by viewModels()
    private lateinit var adapter: AlumnosCursoAdapter

    override fun inflateBinding() = FragmentDetalleCursoDocenteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cursoId = arguments?.getInt("curso_id") ?: 0
        val cursoNombre = arguments?.getString("curso_nombre") ?: "Curso"

        setupRecyclerView()
        setupListeners()
        setupObservers()
        cargarDetalle(cursoId, cursoNombre)
    }

    private fun setupRecyclerView() {
        adapter = AlumnosCursoAdapter { alumno ->
            val bundle = Bundle().apply {
                putInt("alumno_id", alumno.id)
                putString("alumno_nombre", "${alumno.nombre} ${alumno.apellido}")
                putInt("curso_id", arguments?.getInt("curso_id") ?: 0)
                putString("curso_nombre", arguments?.getString("curso_nombre") ?: "Curso")
            }
            findNavController().navigate(
                R.id.action_detalleCursoDocente_to_registrarCalificacion,
                bundle
            )
        }

        binding.rvAlumnos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@DetalleCursoDocenteFragment.adapter
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
        }

        viewModel.detalleCurso.observe(viewLifecycleOwner) { detalle ->
            detalle?.let {
                binding.tvNombreCurso.text = it.nombre
                binding.tvGradoSeccion.text = "${it.grado} - Sección ${it.seccion}"
                binding.tvHorario.text = it.horario ?: "Sin horario"
                binding.tvAula.text = it.aula ?: "Sin aula"
                binding.tvTotalAlumnos.text = "${it.alumnos.size} alumnos"

                if (it.alumnos.isEmpty()) {
                    binding.rvAlumnos.visibility = View.GONE
                    binding.layoutVacio.visibility = View.VISIBLE
                } else {
                    binding.rvAlumnos.visibility = View.VISIBLE
                    binding.layoutVacio.visibility = View.GONE
                    adapter.setData(it.alumnos)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun cargarDetalle(cursoId: Int, cursoNombre: String) {
        viewModel.cargarDetalleCurso(cursoId)
    }
}