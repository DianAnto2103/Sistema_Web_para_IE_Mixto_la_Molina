package com.example.mixtotrackmobile.ui.alumno.solicitartaller.horariodocente

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mixtotrackmobile.R

class HorarioDocenteFragment : Fragment() {

    companion object {
        fun newInstance() = HorarioDocenteFragment()
    }

    private val viewModel: HorarioDocenteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_horario_docente, container, false)
    }
}