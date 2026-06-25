package com.example.mixtotrackmobile.ui.alumno.grupoestudio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.data.models.response.GrupoEstudioResponse

class GrupoEstudioAdapter(
    private val onUnirseClick: (GrupoEstudioResponse) -> Unit,
    private val onSalirClick: (GrupoEstudioResponse) -> Unit,
    private val onEliminarClick: (GrupoEstudioResponse) -> Unit
) : RecyclerView.Adapter<GrupoEstudioAdapter.GrupoViewHolder>() {

    private var grupos: List<GrupoEstudioResponse> = emptyList()

    fun setData(list: List<GrupoEstudioResponse>) {
        grupos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrupoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grupo_estudio, parent, false)
        return GrupoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrupoViewHolder, position: Int) {
        val grupo = grupos[position]
        holder.bind(grupo)

        holder.btnUnirse.setOnClickListener {
            onUnirseClick(grupo)
        }

        holder.btnSalir.setOnClickListener {
            onSalirClick(grupo)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(grupo)
        }
    }

    override fun getItemCount(): Int = grupos.size

    class GrupoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvCurso: TextView = itemView.findViewById(R.id.tvCurso)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        private val tvCreador: TextView = itemView.findViewById(R.id.tvCreador)
        private val tvCupos: TextView = itemView.findViewById(R.id.tvCupos)
        private val tvMiembros: TextView = itemView.findViewById(R.id.tvMiembros)
        private val layoutMiembros: LinearLayout = itemView.findViewById(R.id.layoutMiembros)
        val btnUnirse: Button = itemView.findViewById(R.id.btnUnirse)
        val btnSalir: Button = itemView.findViewById(R.id.btnSalir)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)

        fun bind(grupo: GrupoEstudioResponse) {
            val context = itemView.context

            tvNombre.text = grupo.nombre
            tvCurso.text = grupo.curso
            tvDescripcion.text = grupo.descripcion
            tvCreador.text = "Creado por: ${grupo.creador}"
            tvCupos.text = "${grupo.miembros.size}/${grupo.cupos}"

            // Miembros
            if (grupo.miembros.isNotEmpty()) {
                val nombres = grupo.miembros.take(3).joinToString { it.nombre }
                val resto = grupo.miembros.size - 3
                tvMiembros.text = if (resto > 0) {
                    "$nombres y $resto más"
                } else {
                    nombres
                }
                layoutMiembros.visibility = View.VISIBLE
            } else {
                tvMiembros.text = "Sin miembros"
                layoutMiembros.visibility = View.VISIBLE
            }

            // Botones según estado
            when {
                grupo.esCreador -> {
                    btnUnirse.visibility = View.GONE
                    btnSalir.visibility = View.GONE
                    btnEliminar.visibility = View.VISIBLE
                }
                grupo.esMiembro -> {
                    btnUnirse.visibility = View.GONE
                    btnSalir.visibility = View.VISIBLE
                    btnEliminar.visibility = View.GONE
                }
                grupo.cuposDisponibles <= 0 -> {
                    btnUnirse.visibility = View.GONE
                    btnSalir.visibility = View.GONE
                    btnEliminar.visibility = View.GONE
                }
                else -> {
                    btnUnirse.visibility = View.VISIBLE
                    btnSalir.visibility = View.GONE
                    btnEliminar.visibility = View.GONE
                }
            }
        }
    }
}