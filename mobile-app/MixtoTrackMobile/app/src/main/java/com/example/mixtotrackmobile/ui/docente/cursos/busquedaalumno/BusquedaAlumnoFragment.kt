package com.example.mixtotrackmobile.ui.docente.cursos.busquedaalumno

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mixtotrackmobile.R

class BusquedaAlumnoFragment : Fragment() {

    companion object {
        fun newInstance() = BusquedaAlumnoFragment()
    }

    private val viewModel: BusquedaAlumnoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_busqueda_alumno, container, false)
    }
}