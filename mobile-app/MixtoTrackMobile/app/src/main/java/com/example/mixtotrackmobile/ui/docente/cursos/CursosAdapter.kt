package com.example.mixtotrackmobile.ui.docente.cursos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.CursoDocenteResponse
import java.text.DecimalFormat

class CursosAdapter(
    private val onVerAlumnosClick: (CursoDocenteResponse) -> Unit,
    private val onCalificacionesClick: (CursoDocenteResponse) -> Unit
) : RecyclerView.Adapter<CursosAdapter.CursoViewHolder>() {

    private var cursos: List<CursoDocenteResponse> = emptyList()
    private val df = DecimalFormat("#.#")

    fun setData(list: List<CursoDocenteResponse>) {
        cursos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curso_docente, parent, false)
        return CursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CursoViewHolder, position: Int) {
        val curso = cursos[position]
        holder.bind(curso)

        holder.btnVerAlumnos.setOnClickListener {
            onVerAlumnosClick(curso)
        }

        holder.btnCalificaciones.setOnClickListener {
            onCalificacionesClick(curso)
        }
    }

    override fun getItemCount(): Int = cursos.size

    inner class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreCurso: TextView = itemView.findViewById(R.id.tvNombreCurso)
        private val tvGrado: TextView = itemView.findViewById(R.id.tvGrado)
        private val tvSeccion: TextView = itemView.findViewById(R.id.tvSeccion)
        private val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        private val tvAlumnos: TextView = itemView.findViewById(R.id.tvAlumnos)
        private val tvPromedio: TextView = itemView.findViewById(R.id.tvPromedio)
        private val tvAula: TextView = itemView.findViewById(R.id.tvAula)
        val btnVerAlumnos: Button = itemView.findViewById(R.id.btnVerAlumnos)
        val btnCalificaciones: Button = itemView.findViewById(R.id.btnCalificaciones)

        fun bind(curso: CursoDocenteResponse) {
            tvNombreCurso.text = curso.nombre
            tvGrado.text = curso.grado
            tvSeccion.text = "Sección: ${curso.seccion}"
            tvHorario.text = curso.horario ?: "Sin horario"
            tvAlumnos.text = "${curso.cantidadAlumnos} alumnos"
            tvAula.text = curso.aula ?: "Sin aula"

            val promedio = curso.promedioGeneral
            tvPromedio.text = if (promedio != null) {
                "Prom. ${df.format(promedio)}"
            } else {
                "Sin promedio"
            }
        }
    }
}