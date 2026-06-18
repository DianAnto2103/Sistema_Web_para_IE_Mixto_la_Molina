package com.example.mixtotrackmobile.ui.common.notificaciones

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.NotificacionResponse
import java.text.SimpleDateFormat
import java.util.*

class NotificacionesAdapter(
    private val onItemClick: (NotificacionResponse) -> Unit
) : RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder>() {

    private var notificaciones: List<NotificacionResponse> = emptyList()

    fun setData(list: List<NotificacionResponse>) {
        notificaciones = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)
        return NotificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val notificacion = notificaciones[position]
        holder.bind(notificacion)

        holder.itemView.setOnClickListener {
            onItemClick(notificacion)
        }
    }

    override fun getItemCount(): Int = notificaciones.size

    class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcono: ImageView = itemView.findViewById(R.id.ivIcono)
        private val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        private val tvMensaje: TextView = itemView.findViewById(R.id.tvMensaje)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val viewNoLeida: View = itemView.findViewById(R.id.viewNoLeida)
        private val layoutNotificacion: CardView = itemView.findViewById(R.id.layoutNotificacion)

        fun bind(notificacion: NotificacionResponse) {
            tvTitulo.text = notificacion.titulo
            tvMensaje.text = notificacion.mensaje
            tvFecha.text = formatearFecha(notificacion.fecha)

            // Icono según tipo
            val iconoRes = when (notificacion.tipo) {
                "tarea" -> R.drawable.ic_notification_task
                "calificacion" -> R.drawable.ic_notification_grade
                "taller" -> R.drawable.ic_notification_taller
                else -> R.drawable.ic_notification_info
            }
            ivIcono.setImageResource(iconoRes)

            // Indicador de no leída
            viewNoLeida.visibility = if (notificacion.leida) View.GONE else View.VISIBLE

            // Fondo según estado
            if (!notificacion.leida) {
                layoutNotificacion.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.background_school)
                )
            } else {
                layoutNotificacion.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.white)
                )
            }
        }

        private fun formatearFecha(fecha: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = inputFormat.parse(fecha) ?: Date()

                val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                outputFormat.format(date)
            } catch (e: Exception) {
                fecha
            }
        }
    }
}