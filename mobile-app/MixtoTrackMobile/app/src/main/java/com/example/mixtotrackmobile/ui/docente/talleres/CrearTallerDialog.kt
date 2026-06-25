package com.example.mixtotrackmobile.ui.docente.talleres

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.mixtotrackmobile.R
import com.example.mixtotrackmobile.databinding.DialogCrearTallerBinding
import com.example.mixtotrackmobile.data.repository.TallerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CrearTallerDialog : DialogFragment() {

    @Inject
    lateinit var tallerRepository: TallerRepository

    private lateinit var binding: DialogCrearTallerBinding
    private var onTallerCreado: (() -> Unit)? = null

    fun setOnTallerCreado(callback: () -> Unit) {
        onTallerCreado = callback
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCrearTallerBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Crear Nuevo Taller")
            .setView(binding.root)
            .setPositiveButton("Crear") { _, _ ->
                crearTaller()
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }

    private fun crearTaller() {
        val nombre = binding.etNombre.text.toString().trim()
        val curso = binding.etCurso.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val horario = binding.etHorario.text.toString().trim()
        val fechaInicio = binding.etFechaInicio.text.toString().trim()
        val fechaFin = binding.etFechaFin.text.toString().trim()
        val cuposStr = binding.etCupos.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty()) {
            binding.etNombre.error = "Ingresa el nombre del taller"
            return
        }

        if (curso.isEmpty()) {
            binding.etCurso.error = "Ingresa el curso"
            return
        }

        if (descripcion.isEmpty()) {
            binding.etDescripcion.error = "Ingresa una descripción"
            return
        }

        if (horario.isEmpty()) {
            binding.etHorario.error = "Ingresa el horario"
            return
        }

        if (fechaInicio.isEmpty()) {
            binding.etFechaInicio.error = "Ingresa la fecha de inicio"
            return
        }

        if (fechaFin.isEmpty()) {
            binding.etFechaFin.error = "Ingresa la fecha de fin"
            return
        }

        val cupos = cuposStr.toIntOrNull()
        if (cupos == null || cupos <= 0) {
            binding.etCupos.error = "Ingresa un número válido de cupos"
            return
        }

        // Mostrar loading en el botón
        val dialog = dialog as AlertDialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).text = "Creando..."

        // Llamar al repository
        lifecycleScope.launch {
            val result = tallerRepository.crearTaller(
                nombre = nombre,
                descripcion = descripcion,
                curso = curso,
                horario = horario,
                cupos = cupos,
                fechaInicio = fechaInicio,
                fechaFin = fechaFin
            )

            // Restaurar botón
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).text = "Crear"

            result.fold(
                onSuccess = { taller ->
                    Toast.makeText(requireContext(),
                        "Taller '${taller.nombre}' creado exitosamente",
                        Toast.LENGTH_LONG).show()
                    onTallerCreado?.invoke()
                    dismiss()
                },
                onFailure = { exception ->
                    Toast.makeText(requireContext(),
                        "${exception.message}",
                        Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}