package com.example.mixtotrackmobile.ui.docente.cursos.detallecurso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.AlumnoCursoDetalle
import java.text.DecimalFormat

class AlumnosCursoAdapter(
    private val onRegistrarNotaClick: (AlumnoCursoDetalle) -> Unit
) : RecyclerView.Adapter<AlumnosCursoAdapter.AlumnoViewHolder>() {

    private var alumnos: List<AlumnoCursoDetalle> = emptyList()

    fun setData(list: List<AlumnoCursoDetalle>) {
        alumnos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno_detalle_curso, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = alumnos[position]
        holder.bind(alumno)

        holder.btnRegistrarNota.setOnClickListener {
            onRegistrarNotaClick(alumno)
        }
    }

    override fun getItemCount(): Int = alumnos.size

    class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreAlumno: TextView = itemView.findViewById(R.id.tvNombreAlumno)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        private val tvB1: TextView = itemView.findViewById(R.id.tvB1)
        private val tvB2: TextView = itemView.findViewById(R.id.tvB2)
        private val tvB3: TextView = itemView.findViewById(R.id.tvB3)
        private val tvB4: TextView = itemView.findViewById(R.id.tvB4)
        val btnRegistrarNota: Button = itemView.findViewById(R.id.btnRegistrarNota)

        fun bind(alumno: AlumnoCursoDetalle) {
            val context = itemView.context

            tvNombreAlumno.text = "${alumno.nombre} ${alumno.apellido}"
            tvEmail.text = alumno.email

            val notas = alumno.bimestres.associate { it.bimestre to it.nota }
            val tvNotas = mapOf(1 to tvB1, 2 to tvB2, 3 to tvB3, 4 to tvB4)

            tvNotas.forEach { (bimestre, tv) ->
                val nota = notas[bimestre]
                tv.text = if (nota != null) df.format(nota) else "-"
                val color = when {
                    nota == null -> ContextCompat.getColor(context, R.color.text_secondary)
                    nota >= 16 -> ContextCompat.getColor(context, R.color.success)
                    nota >= 11 -> ContextCompat.getColor(context, R.color.warning)
                    else -> ContextCompat.getColor(context, R.color.secondary)
                }
                tv.setTextColor(color)
            }
        }
    }

    companion object {
        private val df = DecimalFormat("#.#")
    }
}