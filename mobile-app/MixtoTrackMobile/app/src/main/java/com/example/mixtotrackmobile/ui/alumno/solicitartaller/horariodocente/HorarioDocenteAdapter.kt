package com.example.mixtotrackmobile.ui.alumno.solicitartaller.horariodocente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.HorarioResponse

class HorarioDocenteAdapter(
    private val onSeleccionarClick: (HorarioResponse) -> Unit
) : RecyclerView.Adapter<HorarioDocenteAdapter.HorarioViewHolder>() {

    private var horarios: List<HorarioResponse> = emptyList()

    fun setData(list: List<HorarioResponse>) {
        horarios = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario_docente, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.bind(horario)

        holder.btnSeleccionar.setOnClickListener {
            onSeleccionarClick(horario)
        }
    }

    override fun getItemCount(): Int = horarios.size

    class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDocente: TextView = itemView.findViewById(R.id.tvDocente)
        private val tvDiaHora: TextView = itemView.findViewById(R.id.tvDiaHora)
        private val tvTaller: TextView = itemView.findViewById(R.id.tvTaller)
        val btnSeleccionar: Button = itemView.findViewById(R.id.btnSeleccionar)
        private val tvOcupado: TextView = itemView.findViewById(R.id.tvOcupado)

        fun bind(horario: HorarioResponse) {
            tvDocente.text = horario.docente
            tvDiaHora.text = "${horario.dia} ${horario.horaInicio} - ${horario.horaFin}"

            if (horario.taller != null) {
                tvTaller.text = "Taller: ${horario.taller}"
                tvTaller.visibility = View.VISIBLE
            } else {
                tvTaller.visibility = View.GONE
            }

            if (horario.disponible) {
                btnSeleccionar.visibility = View.VISIBLE
                tvOcupado.visibility = View.GONE
                btnSeleccionar.isEnabled = true
            } else {
                btnSeleccionar.visibility = View.GONE
                tvOcupado.visibility = View.VISIBLE
            }
        }
    }
}