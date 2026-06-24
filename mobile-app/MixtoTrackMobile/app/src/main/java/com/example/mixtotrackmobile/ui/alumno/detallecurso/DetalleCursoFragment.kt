package com.example.mixtotrackmobile.ui.alumno.detallecurso

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.BimestreDetalle
import com.example.mixtotrackmobile.databinding.FragmentDetalleCursoBinding
import com.example.mixtotrackmobile.ui.alumno.detalle_curso.DetalleCursoViewModel
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class DetalleCursoFragment : BaseFragment<FragmentDetalleCursoBinding>() {

    private val viewModel: DetalleCursoViewModel by viewModels()
    private val df = DecimalFormat("#.#")

    override fun inflateBinding() = FragmentDetalleCursoBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cursoId = arguments?.getInt("curso_id") ?: 0
        val cursoNombre = arguments?.getString("curso_nombre") ?: "Curso"

        setupListeners()
        setupObservers()
        cargarDetalle(cursoId, cursoNombre)
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnVerRendimiento.setOnClickListener {
            val cursoId = arguments?.getInt("curso_id") ?: 0
            val cursoNombre = arguments?.getString("curso_nombre") ?: "Curso"

            val bundle = Bundle().apply {
                putInt("curso_id", cursoId)
                putString("curso_nombre", cursoNombre)
            }
            try {
                findNavController().navigate(
                    R.id.action_misCursos_to_rendimientoAcademico,
                    bundle
                )
            } catch (e: Exception) {
                android.widget.Toast.makeText(requireContext(),
                    "Error: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.detalleCurso.observe(viewLifecycleOwner) { detalle ->
            detalle?.let {
                // Información del curso
                binding.tvNombreCurso.text = it.nombre
                binding.tvDocente.text = "Prof. ${it.docente}"
                binding.tvHorario.text = it.horario ?: "No disponible"
                binding.tvAula.text = it.aula ?: "No asignada"
                binding.tvDescripcion.text = it.descripcion ?: "Sin descripción"

                // Promedio
                val promedio = it.promedio ?: 0.0
                binding.tvPromedio.text = df.format(promedio)

                val (estadoTexto, colorEstado) = when {
                    promedio >= 16 -> Pair("Aprobado", R.color.success)
                    promedio >= 11 -> Pair("En proceso", R.color.warning)
                    promedio > 0 -> Pair("Desaprobado", R.color.secondary)
                    else -> Pair("Sin notas", R.color.text_secondary)
                }
                binding.tvEstadoPromedio.text = estadoTexto
                binding.tvEstadoPromedio.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), colorEstado)
                )

                // Bimestres
                mostrarBimestres(it.bimestres)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                android.widget.Toast.makeText(requireContext(),
                    "$it", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarBimestres(bimestres: List<BimestreDetalle>) {
        val layout = binding.layoutBimestres
        layout.removeAllViews()

        bimestres.forEachIndexed { index, bimestre ->
            val itemView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_bimestre_detalle, layout, false)

            val tvBimestre: TextView = itemView.findViewById(R.id.tvBimestre)
            val tvNota: TextView = itemView.findViewById(R.id.tvNota)
            val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)

            tvBimestre.text = "Bimestre ${bimestre.bimestre}"

            val nota = bimestre.nota
            tvNota.text = if (nota != null) df.format(nota) else "-"

            val color = when {
                nota == null -> ContextCompat.getColor(requireContext(), R.color.text_secondary)
                nota >= 16 -> ContextCompat.getColor(requireContext(), R.color.success)
                nota >= 11 -> ContextCompat.getColor(requireContext(), R.color.warning)
                else -> ContextCompat.getColor(requireContext(), R.color.secondary)
            }
            tvNota.setTextColor(color)

            val (estadoTexto, estadoColor) = when {
                nota == null -> Pair("Pendiente", R.color.text_secondary)
                nota >= 16 -> Pair("Aprobado", R.color.success)
                nota >= 11 -> Pair("En proceso", R.color.warning)
                else -> Pair("Desaprobado", R.color.secondary)
            }
            tvEstado.text = estadoTexto
            tvEstado.setTextColor(
                ContextCompat.getColor(requireContext(), estadoColor)
            )

            layout.addView(itemView)

            if (index < bimestres.size - 1) {
                val divider = View(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    )
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_200))
                }
                layout.addView(divider)
            }
        }
    }

    private fun cargarDetalle(cursoId: Int, cursoNombre: String) {
        viewModel.cargarDetalleCurso(cursoId)
    }
}