package org.skyfaced.wopi.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentDashboardBinding
import org.skyfaced.wopi.model.adapter.DashboardAdd
import org.skyfaced.wopi.model.adapter.DashboardItem
import org.skyfaced.wopi.ui.BaseFragment
import org.skyfaced.wopi.ui.dashboard.adapter.AddDashboard
import org.skyfaced.wopi.ui.dashboard.adapter.DashboardAdapter
import org.skyfaced.wopi.ui.dashboard.adapter.FilledDashboard
import org.skyfaced.wopi.utils.Response
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {
    private val viewModel by viewModels<DashboardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDashboardAdapter()
        setupSearch()
    }

    private fun setupSearch() {
        binding.btnSearch.setOnClickListener {
            val destination = DashboardFragmentDirections.actionDashboardFragmentToSearchFragment()
            findNavController().navigate(destination)
        }
    }

    private fun setupDashboardAdapter() {
//        val headerDashboardAdapter = DashboardAdapter(listOf(HeaderDashboard()))
        val filledDashboardAdapter = DashboardAdapter(listOf(FilledDashboard(::onFilledItemClick)))
        val addDashboardAdapter = DashboardAdapter(listOf(AddDashboard(::onAddItemClick)))
        val concatAdapter = ConcatAdapter(
//            headerDashboardAdapter,
            filledDashboardAdapter,
            addDashboardAdapter
        )

        val glm = GridLayoutManager(requireContext(), 2)
//        glm.spanSizeLookup = object : SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (position) {
//                    //First item is header
//                    0 -> 2
//                    else -> 1
//                }
//            }
//        }

        binding {
            rvDashboard.layoutManager = glm
            rvDashboard.adapter = concatAdapter
            rvDashboard.postDelayed({
                //            headerDashboardAdapter.submitList(listOf(DashboardHeader("Your today forecast")))
                filledDashboardAdapter.submitList(emptyList())
                addDashboardAdapter.submitList(listOf(DashboardAdd()))
            }, 300L)
        }
    }

    private fun onAddItemClick() {
        Timber.d("Add item clicked")
    }

    private fun onFilledItemClick(dashboardItem: DashboardItem) {
        Timber.d(dashboardItem.toString())
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchLocation.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            ).collect { search ->
                val currentState = when (search) {
                    is Response.Success -> search.data.joinToString()
                    is Response.Error -> search.message
                    is Response.Load -> "Loading..."
                }
                Timber.i(currentState)
            }
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }
}