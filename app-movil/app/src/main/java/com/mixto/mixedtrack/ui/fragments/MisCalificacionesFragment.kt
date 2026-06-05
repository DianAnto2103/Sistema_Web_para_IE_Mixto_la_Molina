package com.mixto.mixedtrack.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mixto.mixedtrack.R
import com.mixto.mixedtrack.databinding.FragmentMisCalificacionesBinding
import com.mixto.mixedtrack.ui.adapter.GradesAdapter
import com.mixto.mixedtrack.ui.viewmodel.MainViewModel
import com.mixto.mixedtrack.util.Resource

class MisCalificacionesFragment : Fragment() {

    private var _binding: FragmentMisCalificacionesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val gradesAdapter = GradesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisCalificacionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeGrades()
        viewModel.loadGrades()
    }

    private fun setupRecyclerView() {
        binding.recyclerGrades.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gradesAdapter
        }
    }

    private fun observeGrades() {
        lifecycleScope.launchWhenStarted {
            viewModel.gradesState.collect { state ->
                when (state) {
                    Resource.Idle -> showLoading(false)
                    Resource.Loading -> showLoading(true)
                    is Resource.Success -> showGrades(state.data)
                    is Resource.Error -> showError(state.message)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.textMessage.visibility = View.GONE
        binding.recyclerGrades.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showGrades(grades: List<com.mixto.mixedtrack.data.model.Grade>) {
        showLoading(false)
        if (grades.isEmpty()) {
            binding.textMessage.text = getString(com.mixto.mixedtrack.R.string.message_no_data_grades)
            binding.textMessage.visibility = View.VISIBLE
            binding.recyclerGrades.visibility = View.GONE
            return
        }
        gradesAdapter.submitList(grades)
        binding.recyclerGrades.visibility = View.VISIBLE
        binding.textMessage.visibility = View.GONE
    }

    private fun showError(message: String) {
        showLoading(false)
        binding.textMessage.text = message
        binding.textMessage.visibility = View.VISIBLE
        binding.recyclerGrades.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
