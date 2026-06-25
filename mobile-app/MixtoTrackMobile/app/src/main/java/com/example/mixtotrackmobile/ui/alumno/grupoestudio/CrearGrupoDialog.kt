package com.example.mixtotrackmobile.ui.alumno.grupoestudio

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.DialogCrearGrupoBinding

class CrearGrupoDialog(
    private val onCrearClick: (String, String, String, Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogCrearGrupoBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCrearGrupoBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Crear Grupo de Estudio")
            .setView(binding.root)
            .setPositiveButton("Crear") { _, _ ->
                val nombre = binding.etNombre.text.toString().trim()
                val curso = binding.etCurso.text.toString().trim()
                val descripcion = binding.etDescripcion.text.toString().trim()
                val cupos = binding.etCupos.text.toString().toIntOrNull() ?: 5

                if (validarCampos(nombre, curso, descripcion)) {
                    onCrearClick(nombre, curso, descripcion, cupos)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    private fun validarCampos(nombre: String, curso: String, descripcion: String): Boolean {
        return when {
            nombre.isEmpty() -> {
                Toast.makeText(requireContext(), "Ingresa un nombre", Toast.LENGTH_SHORT).show()
                false
            }
            curso.isEmpty() -> {
                Toast.makeText(requireContext(), "Ingresa un curso", Toast.LENGTH_SHORT).show()
                false
            }
            descripcion.isEmpty() -> {
                Toast.makeText(requireContext(), "Ingresa una descripción", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}