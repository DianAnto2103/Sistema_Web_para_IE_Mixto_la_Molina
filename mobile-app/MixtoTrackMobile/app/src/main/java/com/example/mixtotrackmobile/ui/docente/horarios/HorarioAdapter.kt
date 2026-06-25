package com.example.mixtotrackmobile.ui.docente.horarios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.HorarioResponse

class HorariosAdapter(
    private val onDisponibilidadChange: (HorarioResponse, Boolean) -> Unit
) : RecyclerView.Adapter<HorariosAdapter.HorarioViewHolder>() {

    private var horarios: List<HorarioResponse> = emptyList()

    fun setData(list: List<HorarioResponse>) {
        horarios = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.bind(horario)

        holder.switchDisponible.setOnCheckedChangeListener { _, isChecked ->
            onDisponibilidadChange(horario, isChecked)
        }
    }

    override fun getItemCount(): Int = horarios.size

    class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDia: TextView = itemView.findViewById(R.id.tvDia)
        private val tvHoraInicio: TextView = itemView.findViewById(R.id.tvHoraInicio)
        private val tvHoraFin: TextView = itemView.findViewById(R.id.tvHoraFin)
        private val tvTaller: TextView = itemView.findViewById(R.id.tvTaller)
        val switchDisponible: SwitchCompat = itemView.findViewById(R.id.switchDisponible)

        fun bind(horario: HorarioResponse) {
            tvDia.text = horario.dia
            tvHoraInicio.text = horario.horaInicio
            tvHoraFin.text = horario.horaFin

            if (horario.taller != null) {
                tvTaller.text = "Taller: ${horario.taller}"
                tvTaller.visibility = View.VISIBLE
            } else {
                tvTaller.visibility = View.GONE
            }

            switchDisponible.isChecked = horario.disponible
        }
    }
}