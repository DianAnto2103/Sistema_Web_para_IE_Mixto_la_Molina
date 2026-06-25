package com.example.mixtotrackmobile.ui.docente.talleres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.TallerDocenteResponse

class TalleresDocenteAdapter(
    private val onVerSolicitudesClick: (TallerDocenteResponse) -> Unit,
    private val onCambiarEstadoClick: (TallerDocenteResponse) -> Unit,
    private val onEliminarClick: (TallerDocenteResponse) -> Unit
) : RecyclerView.Adapter<TalleresDocenteAdapter.TallerViewHolder>() {

    private var talleres: List<TallerDocenteResponse> = emptyList()

    fun setData(list: List<TallerDocenteResponse>) {
        talleres = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_taller_docente, parent, false)
        return TallerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val taller = talleres[position]
        holder.bind(taller)

        holder.btnVerSolicitudes.setOnClickListener {
            onVerSolicitudesClick(taller)
        }

        holder.btnCambiarEstado.setOnClickListener {
            onCambiarEstadoClick(taller)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(taller)
        }
    }

    override fun getItemCount(): Int = talleres.size

    class TallerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        private val tvCurso: TextView = itemView.findViewById(R.id.tvCurso)
        private val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        private val tvCupos: TextView = itemView.findViewById(R.id.tvCupos)
        private val tvSolicitudes: TextView = itemView.findViewById(R.id.tvSolicitudes)
        val btnVerSolicitudes: Button = itemView.findViewById(R.id.btnVerSolicitudes)
        val btnCambiarEstado: Button = itemView.findViewById(R.id.btnCambiarEstado)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)

        fun bind(taller: TallerDocenteResponse) {
            val context = itemView.context

            tvNombre.text = taller.nombre
            tvCurso.text = taller.curso
            tvHorario.text = taller.horario
            tvCupos.text = "Cupos: ${taller.cuposDisponibles}/${taller.cupos}"
            tvSolicitudes.text = "Solicitudes: ${taller.solicitudes}"

            // Estado
            val (estadoTexto, colorRes) = when (taller.estado) {
                "activo" -> Pair("Activo", R.drawable.bg_estado_disponible)
                "inactivo" -> Pair("Inactivo", R.drawable.bg_estado_completo)
                "completo" -> Pair("Completo", R.drawable.bg_estado_completo)
                else -> Pair("Desconocido", R.drawable.bg_estado_completo)
            }
            tvEstado.text = estadoTexto
            tvEstado.setBackgroundResource(colorRes)

            // Color de estado
            val color = when (taller.estado) {
                "activo" -> ContextCompat.getColor(context, R.color.success)
                else -> ContextCompat.getColor(context, R.color.secondary)
            }
            tvEstado.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}