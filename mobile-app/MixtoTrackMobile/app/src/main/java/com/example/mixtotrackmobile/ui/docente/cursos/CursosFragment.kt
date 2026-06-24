package com.example.mixtotrackmobile.ui.docente.cursos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.FragmentCursosBinding
import com.example.mixtotrackmobile.data.models.entities.Curso
import com.example.mixtotrackmobile.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CursosFragment : Fragment() {

    private var _binding: FragmentCursosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CursosViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCursosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombreCompleto = sessionManager.getNombreCompleto()
        binding.tvNombreDocente.text = nombreCompleto ?: "Profesor"

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setupObservers()

        val docenteId = sessionManager.getDocenteId()
        if (docenteId != -1) {
            viewModel.cargarCursos(docenteId)
        } else {
            Toast.makeText(requireContext(), "Error al obtener ID del docente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.cursos.observe(viewLifecycleOwner) { cursos ->
            if (cursos.isNotEmpty()) {
                mostrarCursos(cursos)
                actualizarEstadisticas(cursos)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun actualizarEstadisticas(cursos: List<Curso>) {
        binding.tvCursosCount.text = cursos.size.toString()
        binding.tvTotalEstudiantes.text = "0"  // No tenemos este dato
        binding.tvHorasSemanales.text = "0"    // No tenemos este dato
    }


    private fun mostrarCursos(cursos: List<Curso>) {
        binding.containerCursos.removeAllViews()

        if (cursos.isEmpty()) {
            val tvVacio = TextView(requireContext()).apply {
                text = "No tienes cursos asignados"
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                gravity = View.Gravity.CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, 40, 0, 40)
            }
            binding.containerCursos.addView(tvVacio)
            return
        }

        cursos.forEach { curso ->
            val itemView = layoutInflater.inflate(R.layout.item_curso, binding.containerCursos, false)

            val tvNombreCurso = itemView.findViewById<TextView>(R.id.tvNombreCurso)
            val tvPromedioCurso = itemView.findViewById<TextView>(R.id.tvPromedioCurso)
            val tvDocente = itemView.findViewById<TextView>(R.id.tvDocente)
            val tvB1 = itemView.findViewById<TextView>(R.id.tvB1)
            val tvB2 = itemView.findViewById<TextView>(R.id.tvB2)
            val tvB3 = itemView.findViewById<TextView>(R.id.tvB3)
            val tvB4 = itemView.findViewById<TextView>(R.id.tvB4)
            val btnVerDetalle = itemView.findViewById<View>(R.id.btnVerDetalle)

            // Usando los campos REALES de Curso
            tvNombreCurso.text = curso.nombre
            tvDocente.text = "Docente ID: ${curso.docente}"
            tvPromedioCurso.text = "-"  // No tenemos promedio

            // Bimestres
            tvB1.text = "-"
            tvB2.text = "-"
            tvB3.text = "-"
            tvB4.text = "-"

            itemView.setOnClickListener {
                Toast.makeText(requireContext(), "Curso: ${curso.nombre}", Toast.LENGTH_SHORT).show()
            }

            btnVerDetalle.setOnClickListener {
                Toast.makeText(requireContext(), "Ver detalle de: ${curso.nombre}", Toast.LENGTH_SHORT).show()
            }

            binding.containerCursos.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}