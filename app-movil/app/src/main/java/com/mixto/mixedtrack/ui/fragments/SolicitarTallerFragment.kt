package com.mixto.mixedtrack.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mixto.mixedtrack.R
import com.mixto.mixedtrack.databinding.FragmentSolicitarTallerBinding
import com.mixto.mixedtrack.ui.adapter.WorkshopsAdapter
import com.mixto.mixedtrack.ui.viewmodel.MainViewModel
import com.mixto.mixedtrack.util.Resource

class SolicitarTallerFragment : Fragment() {

    private var _binding: FragmentSolicitarTallerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var workshopsAdapter: WorkshopsAdapter
    private var selectedWorkshopId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolicitarTallerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeWorkshopState()
        binding.buttonRequest.setOnClickListener {
            selectedWorkshopId?.let { workshopId ->
                viewModel.requestWorkshop(workshopId)
            } ?: Toast.makeText(requireContext(), "Selecciona un taller antes de solicitar", Toast.LENGTH_SHORT).show()
        }
        viewModel.loadWorkshops()
    }

    private fun setupRecyclerView() {
        workshopsAdapter = WorkshopsAdapter(emptyList()) { workshop ->
            selectedWorkshopId = workshop.id
            binding.buttonRequest.isEnabled = true
        }
        binding.recyclerWorkshops.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workshopsAdapter
        }
    }

    private fun observeWorkshopState() {
        lifecycleScope.launchWhenStarted {
            viewModel.workshopsState.collect { state ->
                when (state) {
                    Resource.Idle -> showLoading(false)
                    Resource.Loading -> showLoading(true)
                    is Resource.Success -> showWorkshops(state.data)
                    is Resource.Error -> showError(state.message)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.requestState.collect { state ->
                when (state) {
                    Resource.Idle -> binding.progressBarRequest.visibility = View.GONE
                    Resource.Loading -> binding.progressBarRequest.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBarRequest.visibility = View.GONE
                        Toast.makeText(requireContext(), state.data, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        binding.progressBarRequest.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.recyclerWorkshops.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.textMessage.visibility = View.GONE
        binding.buttonRequest.isEnabled = !isLoading && selectedWorkshopId != null
    }

    private fun showWorkshops(workshops: List<com.mixto.mixedtrack.data.model.Workshop>) {
        if (workshops.isEmpty()) {
            binding.textMessage.text = getString(com.mixto.mixedtrack.R.string.message_no_data_workshops)
            binding.textMessage.visibility = View.VISIBLE
            binding.recyclerWorkshops.visibility = View.GONE
            binding.buttonRequest.isEnabled = false
            return
        }
        workshopsAdapter.submitList(workshops)
        binding.recyclerWorkshops.visibility = View.VISIBLE
        binding.textMessage.visibility = View.GONE
        binding.buttonRequest.isEnabled = selectedWorkshopId != null
    }

    private fun showError(message: String) {
        binding.textMessage.text = message
        binding.textMessage.visibility = View.VISIBLE
        binding.recyclerWorkshops.visibility = View.GONE
        binding.buttonRequest.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
