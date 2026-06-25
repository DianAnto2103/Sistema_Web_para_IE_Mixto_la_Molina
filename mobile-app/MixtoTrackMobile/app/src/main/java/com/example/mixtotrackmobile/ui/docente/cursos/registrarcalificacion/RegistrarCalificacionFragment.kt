package com.example.mixtotrackmobile.ui.docente.cursos.registrarcalificacion

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentRegistrarCalificacionBinding
import com.example.mixtotrackmobile.ui.base.BaseFragment
import com.example.mixtotrackmobile.ui.docente.cursos.registrar_calificacion.RegistrarCalificacionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrarCalificacionFragment : BaseFragment<FragmentRegistrarCalificacionBinding>() {

    private val viewModel: RegistrarCalificacionViewModel by viewModels()

    override fun inflateBinding() = FragmentRegistrarCalificacionBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener argumentos
        val alumnoId = arguments?.getInt("alumno_id") ?: 0
        val alumnoNombre = arguments?.getString("alumno_nombre") ?: "Alumno"
        val cursoId = arguments?.getInt("curso_id") ?: 0
        val cursoNombre = arguments?.getString("curso_nombre") ?: "Curso"
        val grado = arguments?.getString("grado") ?: ""
        val seccion = arguments?.getString("seccion") ?: ""

        setupUI(alumnoNombre, cursoNombre, grado, seccion)
        setupListeners()
        setupObservers(alumnoId, cursoId)
    }

    private fun setupUI(alumnoNombre: String, cursoNombre: String, grado: String, seccion: String) {
        binding.tvAlumnoNombre.text = "Alumno: $alumnoNombre"
        binding.tvCursoNombre.text = "Curso: $cursoNombre"
        binding.tvGradoSeccion.text = "$grado - Sección $seccion"
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnRegistrar.setOnClickListener {
            registrarCalificacion()
        }

        binding.btnRegistrarOtra.setOnClickListener {
            limpiarFormulario()
        }
    }

    private fun setupObservers(alumnoId: Int, cursoId: Int) {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnRegistrar.isEnabled = !loading
        }

        viewModel.exito.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                binding.layoutExito.visibility = View.VISIBLE
                binding.btnRegistrar.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registrarCalificacion() {
        val bimestreStr = binding.etBimestre.text.toString().trim()
        val notaStr = binding.etNota.text.toString().trim()

        if (bimestreStr.isEmpty()) {
            binding.etBimestre.error = "Ingresa el bimestre"
            return
        }

        if (notaStr.isEmpty()) {
            binding.etNota.error = "Ingresa la nota"
            return
        }

        val bimestre = bimestreStr.toIntOrNull()
        if (bimestre == null || bimestre !in 1..4) {
            binding.etBimestre.error = "Bimestre debe ser 1, 2, 3 o 4"
            return
        }

        val nota = notaStr.toDoubleOrNull()
        if (nota == null || nota < 0 || nota > 20) {
            binding.etNota.error = "Nota debe estar entre 0 y 20"
            return
        }

        val alumnoId = arguments?.getInt("alumno_id") ?: 0
        val cursoId = arguments?.getInt("curso_id") ?: 0

        viewModel.registrarCalificacion(alumnoId, cursoId, bimestre, nota)
    }

    private fun limpiarFormulario() {
        binding.etBimestre.text?.clear()
        binding.etNota.text?.clear()
        binding.etBimestre.error = null
        binding.etNota.error = null
        binding.layoutExito.visibility = View.GONE
        binding.btnRegistrar.visibility = View.VISIBLE
        viewModel.resetearEstado()
    }
}