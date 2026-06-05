package com.mixto.mixedtrack.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mixto.mixedtrack.data.model.Workshop
import com.mixto.mixedtrack.databinding.ItemWorkshopBinding

class WorkshopsAdapter(
    private var workshops: List<Workshop> = emptyList(),
    private val onSelectWorkshop: (Workshop) -> Unit
) : RecyclerView.Adapter<WorkshopsAdapter.WorkshopViewHolder>() {

    private var selectedWorkshopId: Int? = null

    fun submitList(newWorkshops: List<Workshop>) {
        workshops = newWorkshops
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkshopViewHolder {
        val binding = ItemWorkshopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkshopViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkshopViewHolder, position: Int) {
        holder.bind(workshops[position])
    }

    override fun getItemCount(): Int = workshops.size

    fun selectWorkshop(workshopId: Int) {
        selectedWorkshopId = workshopId
        notifyDataSetChanged()
    }

    inner class WorkshopViewHolder(private val binding: ItemWorkshopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workshop: Workshop) {
            binding.textWorkshopName.text = workshop.nombre
            binding.textWorkshopDescription.text = workshop.descripcion
            binding.textWorkshopSeats.text = "Cupos: ${workshop.cupos}"
            binding.root.isSelected = workshop.id == selectedWorkshopId

            binding.root.setOnClickListener {
                selectWorkshop(workshop.id)
                onSelectWorkshop(workshop)
            }
        }
    }
}
