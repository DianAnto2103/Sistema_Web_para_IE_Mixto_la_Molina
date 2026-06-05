package com.mixto.mixedtrack.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mixto.mixedtrack.data.model.Grade
import com.mixto.mixedtrack.databinding.ItemGradeBinding

class GradesAdapter(
    private var grades: List<Grade> = emptyList()
) : RecyclerView.Adapter<GradesAdapter.GradeViewHolder>() {

    fun submitList(newGrades: List<Grade>) {
        grades = newGrades
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val binding = ItemGradeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GradeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        holder.bind(grades[position])
    }

    override fun getItemCount(): Int = grades.size

    inner class GradeViewHolder(private val binding: ItemGradeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(grade: Grade) {
            binding.textCourse.text = grade.curso
            binding.textTerm.text = grade.bimestre
            binding.textGrade.text = grade.nota
        }
    }
}
