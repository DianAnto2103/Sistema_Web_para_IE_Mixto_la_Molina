package com.example.mixtotrackmobile.ui.alumno.mis_cursos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.CursoResponse
import java.text.DecimalFormat

class MisCursosAdapter(
    private val onVerDetalleClick: (CursoResponse) -> Unit
) : RecyclerView.Adapter<MisCursosAdapter.CursoViewHolder>() {

    private var cursos: List<CursoResponse> = emptyList()

    private val df = DecimalFormat("#.#")

    fun setData(list: List<CursoResponse>) {
        cursos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]
        holder.bind(curso)

        holder.btnVerDetalle.setOnClickListener {
            onVerDetalleClick(curso)
        }
    }

    override fun getItemCount(): Int = cursos.size

    class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreCurso: TextView = itemView.findViewById(R.id.tvNombreCurso)
        private val tvPromedioCurso: TextView = itemView.findViewById(R.id.tvPromedioCurso)
        private val tvDocente: TextView = itemView.findViewById(R.id.tvDocente)
        private val tvB1: TextView = itemView.findViewById(R.id.tvB1)
        private val tvB2: TextView = itemView.findViewById(R.id.tvB2)
        private val tvB3: TextView = itemView.findViewById(R.id.tvB3)
        private val tvB4: TextView = itemView.findViewById(R.id.tvB4)
        val btnVerDetalle: Button = itemView.findViewById(R.id.btnVerDetalle)

        fun bind(curso: CursoResponse) {
            val context = itemView.context
            val df = DecimalFormat("#.#")

            tvNombreCurso.text = curso.nombre
            tvDocente.text = curso.docente

            // Promedio
            val promedio = curso.promedio ?: 0.0
            tvPromedioCurso.text = df.format(promedio)

            // Color del promedio
            val colorPromedio = when {
                promedio >= 16 -> ContextCompat.getColor(context, R.color.success)
                promedio >= 11 -> ContextCompat.getColor(context, R.color.warning)
                promedio > 0 -> ContextCompat.getColor(context, R.color.secondary)
                else -> ContextCompat.getColor(context, R.color.text_secondary)
            }
            tvPromedioCurso.setTextColor(colorPromedio)
            tvPromedioCurso.background = ContextCompat.getDrawable(context, R.drawable.bg_promedio)
            tvPromedioCurso.background?.setTint(
                if (promedio > 0) colorPromedio else ContextCompat.getColor(context, R.color.gray_200)
            )

            // Bimestres
            val bimestres = curso.bimestres ?: emptyList()
            val notas = mapOf(
                1 to tvB1,
                2 to tvB2,
                3 to tvB3,
                4 to tvB4
            )

            // Inicializar todos con "-"
            notas.values.forEach { it.text = "-" }

            // Llenar con notas
            bimestres.forEach { bimestre ->
                val nota = bimestre.nota
                val tv = notas[bimestre.bimestre]
                tv?.apply {
                    text = if (nota != null) df.format(nota) else "-"
                    val color = when {
                        nota == null -> ContextCompat.getColor(context, R.color.text_secondary)
                        nota >= 16 -> ContextCompat.getColor(context, R.color.success)
                        nota >= 11 -> ContextCompat.getColor(context, R.color.warning)
                        else -> ContextCompat.getColor(context, R.color.secondary)
                    }
                    setTextColor(color)
                }
            }
        }
    }
}