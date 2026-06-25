package com.example.mixtotrackmobile.ui.alumno.mis_talleres

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.SolicitudTallerResponse

class MisTalleresAdapter(
    private val onTallerClick: (SolicitudTallerResponse) -> Unit
) : RecyclerView.Adapter<MisTalleresAdapter.TallerViewHolder>() {

    private var talleres: List<SolicitudTallerResponse> = emptyList()
    private var filtro: String = "todos"

    fun setData(list: List<SolicitudTallerResponse>, filtro: String = "todos") {
        this.filtro = filtro
        talleres = when (filtro) {
            "todos" -> list
            else -> list.filter { it.estado == filtro }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mis_taller, parent, false)
        return TallerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val taller = talleres[position]
        holder.bind(taller)

        holder.itemView.setOnClickListener {
            onTallerClick(taller)
        }
    }

    override fun getItemCount(): Int = talleres.size

    class TallerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        private val tvDocente: TextView = itemView.findViewById(R.id.tvDocente)
        private val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val tvMotivo: TextView = itemView.findViewById(R.id.tvMotivo)

        fun bind(taller: SolicitudTallerResponse) {
            val context = itemView.context

            tvNombre.text = taller.tallerNombre
            tvDocente.text = taller.docente
            tvHorario.text = taller.horario
            tvFecha.text = "Solicitado: ${taller.fechaSolicitud}"

            // Estado
            val (estadoTexto, colorRes) = when (taller.estado) {
                "pendiente" -> Pair("Pendiente", R.drawable.bg_estado_pendiente)
                "aprobado" -> Pair("Aprobado", R.drawable.bg_estado_aprobado)
                "rechazado" -> Pair("Rechazado", R.drawable.bg_estado_rechazado)
                else -> Pair("Desconocido", R.drawable.bg_estado_pendiente)
            }
            tvEstado.text = estadoTexto
            tvEstado.setBackgroundResource(colorRes)

            // Motivo (solo si está rechazado)
            if (taller.estado == "rechazado" && taller.motivo != null) {
                tvMotivo.text = "Motivo: ${taller.motivo}"
                tvMotivo.visibility = View.VISIBLE
            } else {
                tvMotivo.visibility = View.GONE
            }
        }
    }
}