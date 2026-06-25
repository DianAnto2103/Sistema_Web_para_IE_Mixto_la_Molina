package com.example.mixtotrackmobile.ui.alumno.miscursos.rendimientoacademico

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.BimestreRendimiento
import com.example.mixtotrackmobile.databinding.FragmentRendimientoAcademicoBinding
import com.example.mixtotrackmobile.ui.alumno.mis_cursos.rendimiento_academico.RendimientoAcademicoViewModel
import com.example.mixtotrackmobile.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class RendimientoAcademicoFragment : BaseFragment<FragmentRendimientoAcademicoBinding>() {

    private val viewModel: RendimientoAcademicoViewModel by viewModels()
    private val df = DecimalFormat("#.#")

    override fun inflateBinding() = FragmentRendimientoAcademicoBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cursoId = arguments?.getInt("curso_id") ?: 0
        val cursoNombre = arguments?.getString("curso_nombre") ?: "Curso"

        setupListeners()
        setupObservers()
        cargarRendimiento(cursoId, cursoNombre)
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

        viewModel.rendimiento.observe(viewLifecycleOwner) { rendimiento ->
            rendimiento?.let {
                binding.tvCursoNombre.text = it.cursoNombre
                binding.tvDocente.text = "Prof. ${it.docente}"

                val promedio = it.promedioGeneral
                binding.tvPromedioGeneral.text = df.format(promedio)

                val (estadoTexto, colorEstado) = when {
                    promedio >= 16 -> Pair("Aprobado", R.color.success)
                    promedio >= 11 -> Pair("En proceso", R.color.warning)
                    else -> Pair("Desaprobado", R.color.secondary)
                }
                binding.tvEstadoGeneral.text = estadoTexto
                binding.tvEstadoGeneral.setTextColor(
                    ContextCompat.getColor(requireContext(), colorEstado)
                )
                binding.tvPromedioGeneral.setTextColor(
                    ContextCompat.getColor(requireContext(), colorEstado)
                )

                graficarBimestres(it.bimestres)
                mostrarDetalleBimestres(it.bimestres)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                android.widget.Toast.makeText(requireContext(), "❌ $it", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun graficarBimestres(bimestres: List<BimestreRendimiento>) {
        val layout = binding.layoutGrafico
        layout.removeAllViews()

        val maxNota = 20f
        val maxAltura = 160f

        bimestres.forEach { bimestre ->
            val nota = (bimestre.nota ?: 0.0).toFloat()
            val altura = (nota / maxNota * maxAltura).coerceAtLeast(10f)
            val color = when {
                nota >= 16 -> ContextCompat.getColor(requireContext(), R.color.success)
                nota >= 11 -> ContextCompat.getColor(requireContext(), R.color.warning)
                nota > 0 -> ContextCompat.getColor(requireContext(), R.color.secondary)
                else -> ContextCompat.getColor(requireContext(), R.color.gray_400)
            }

            val barContainer = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            }

            // Barra
            val barView = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    dpToPx(36f),
                    dpToPx(altura)
                )
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_barra_nota)
                background?.setTint(color)
            }

            // Etiqueta con nota
            val tvNota = TextView(requireContext()).apply {
                text = if (nota > 0) df.format(nota.toDouble()) else "-"
                textSize = 12f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Etiqueta con bimestre
            val tvBimestre = TextView(requireContext()).apply {
                text = "B${bimestre.bimestre}"
                textSize = 11f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Espaciador
            val spacer = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
            }

            barContainer.addView(spacer)
            barContainer.addView(barView)
            barContainer.addView(tvNota)
            barContainer.addView(tvBimestre)

            layout.addView(barContainer)
        }
    }

    private fun mostrarDetalleBimestres(bimestres: List<BimestreRendimiento>) {
        val layout = binding.layoutDetalle
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

    private fun dpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun cargarRendimiento(cursoId: Int, cursoNombre: String) {
        viewModel.cargarRendimiento(cursoId)
    }
}