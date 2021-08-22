package org.skyfaced.wopi.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentDashboardBinding
import org.skyfaced.wopi.model.adapter.DashboardAdd
import org.skyfaced.wopi.model.adapter.DashboardHeader
import org.skyfaced.wopi.model.adapter.DashboardItem
import org.skyfaced.wopi.ui.base.BaseAdapter
import org.skyfaced.wopi.ui.base.BaseFragment
import org.skyfaced.wopi.ui.dashboard.adapter.AddDashboard
import org.skyfaced.wopi.ui.dashboard.adapter.FilledDashboard
import org.skyfaced.wopi.ui.dashboard.adapter.HeaderDashboard
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {
    private val viewModel by viewModels<DashboardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupSearch()
    }

    private fun setupSearch() {
        binding.btnSearch.setOnClickListener {
            val destination = DashboardFragmentDirections.actionDashboardFragmentToSearchFragment()
            findNavController().navigate(destination)
        }
    }

    private fun setupRecycler() {
        binding {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerDashboard.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY && btnSearch.isExtended) {
                        btnSearch.shrink()
                    }

                    if (scrollY < oldScrollY && !btnSearch.isExtended) {
                        btnSearch.extend()
                    }
                }
            } else {
                recyclerDashboard.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(view, dx, dy)
                        if (dy > 0 && btnSearch.isExtended) {
                            btnSearch.shrink()
                        }

                        if (dy < 0 && !btnSearch.isExtended) {
                            btnSearch.extend()
                        }
                    }
                })
            }

            val glm = GridLayoutManager(requireContext(), 2)
            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 2
                        else -> 1
                    }
                }
            }
            recyclerDashboard.layoutManager = glm

            val headerDashboardAdapter = BaseAdapter(listOf(HeaderDashboard()))
            val filledDashboardAdapter = BaseAdapter(listOf(FilledDashboard(::onFilledItemClick)))
            val addDashboardAdapter = BaseAdapter(listOf(AddDashboard(::onAddItemClick)))
            val concatAdapter = ConcatAdapter(
                headerDashboardAdapter,
                filledDashboardAdapter,
                addDashboardAdapter
            )
            recyclerDashboard.adapter = concatAdapter

            recyclerDashboard.postDelayed({
                headerDashboardAdapter.submitList(listOf(DashboardHeader("Your today forecast")))
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

    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }
}