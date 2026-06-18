package com.example.mixtotrackmobile.ui.alumno.solicitar_taller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.TallerResponse

class TalleresAdapter(
    private val onSolicitarClick: (TallerResponse) -> Unit
) : RecyclerView.Adapter<TalleresAdapter.TallerViewHolder>() {

    private var talleres: List<TallerResponse> = emptyList()

    fun setData(list: List<TallerResponse>) {
        talleres = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TallerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_taller, parent, false)
        return TallerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TallerViewHolder, position: Int) {
        val taller = talleres[position]
        holder.bind(taller)

        holder.btnSolicitar.setOnClickListener {
            onSolicitarClick(taller)
        }
    }

    override fun getItemCount(): Int = talleres.size

    class TallerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        private val tvDocente: TextView = itemView.findViewById(R.id.tvDocente)
        private val tvHorario: TextView = itemView.findViewById(R.id.tvHorario)
        private val tvCupos: TextView = itemView.findViewById(R.id.tvCupos)
        val btnSolicitar: Button = itemView.findViewById(R.id.btnSolicitar)
        private val tvEstadoSolicitud: TextView = itemView.findViewById(R.id.tvEstadoSolicitud)

        fun bind(taller: TallerResponse) {
            tvNombre.text = taller.nombre
            tvDescripcion.text = taller.descripcion
            tvDocente.text = "Prof. ${taller.docente}"
            tvHorario.text = taller.horario
            tvCupos.text = "${taller.cuposDisponibles} cupos disponibles"

            // Estado del taller
            when (taller.estado) {
                "disponible" -> {
                    tvEstado.text = "Disponible"
                    tvEstado.setBackgroundResource(R.drawable.bg_estado_disponible)
                    btnSolicitar.isEnabled = true
                    btnSolicitar.visibility = View.VISIBLE
                    tvEstadoSolicitud.visibility = View.GONE
                }
                "completo" -> {
                    tvEstado.text = "Completo"
                    tvEstado.setBackgroundResource(R.drawable.bg_estado_completo)
                    btnSolicitar.isEnabled = false
                    btnSolicitar.visibility = View.GONE
                    tvEstadoSolicitud.visibility = View.VISIBLE
                    tvEstadoSolicitud.text = "Sin cupos disponibles"
                    tvEstadoSolicitud.setTextColor(
                        ContextCompat.getColor(itemView.context, R.color.secondary)
                    )
                }
            }
        }
    }
}