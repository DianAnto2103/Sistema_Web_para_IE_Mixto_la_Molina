package com.example.mixtotrackmobile.ui.docente.cursos.busquedaalumno

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.AlumnoResponse
import java.text.DecimalFormat

class AlumnoBusquedaAdapter(
    private val onVerCalificacionesClick: (AlumnoResponse) -> Unit
) : RecyclerView.Adapter<AlumnoBusquedaAdapter.AlumnoViewHolder>() {

    private var alumnos: List<AlumnoResponse> = emptyList()
    private val df = DecimalFormat("#.#")

    fun setData(list: List<AlumnoResponse>) {
        alumnos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno_busqueda, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = alumnos[position]
        holder.bind(alumno)

        holder.btnVerCalificaciones.setOnClickListener {
            onVerCalificacionesClick(alumno)
        }
    }

    override fun getItemCount(): Int = alumnos.size

    inner class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreCompleto: TextView = itemView.findViewById(R.id.tvNombreCompleto)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        private val tvGrado: TextView = itemView.findViewById(R.id.tvGrado)
        private val tvPromedio: TextView = itemView.findViewById(R.id.tvPromedio)
        private val tvSeccion: TextView = itemView.findViewById(R.id.tvSeccion)
        val btnVerCalificaciones: Button = itemView.findViewById(R.id.btnVerCalificaciones)

        fun bind(alumno: AlumnoResponse) {
            tvNombreCompleto.text = "${alumno.nombre} ${alumno.apellido}"
            tvEmail.text = alumno.email
            tvGrado.text = alumno.grado
            tvSeccion.text = "Sección: ${alumno.seccion}"

            val promedio = alumno.promedioGeneral
            tvPromedio.text = if (promedio != null) {
                "Promedio: ${df.format(promedio)}"
            } else {
                "Sin promedio"
            }
        }
    }
}