package org.skyfaced.wopi.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.FragmentDashboardBinding
import org.skyfaced.wopi.model.adapter.DashboardHeader
import org.skyfaced.wopi.model.adapter.DashboardItem
import org.skyfaced.wopi.ui.base.BaseAdapter
import org.skyfaced.wopi.ui.base.BaseFragment
import org.skyfaced.wopi.ui.dashboard.adapter.FilledDashboard
import org.skyfaced.wopi.ui.dashboard.adapter.HeaderDashboard
import org.skyfaced.wopi.utils.SwipeToRemove
import org.skyfaced.wopi.utils.extensions.lazySafetyNone
import org.skyfaced.wopi.utils.extensions.onClick

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {
    private val viewModel by viewModels<DashboardViewModel>()

    private val filledDashboardAdapter by lazySafetyNone {
        BaseAdapter(listOf(FilledDashboard(viewModel::onDetailItemClicked)))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            btnSearch.onClick {
                viewModel.onSearchClicked()
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.retry()
            }

            setupRecycler()
        }

        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dashboardEvent.flowWithLifecycle(lifecycle).collect { event ->
                when (event) {
                    is DashboardEvent.PopulateDashboardItems -> {
                        binding.swipeRefresh.isRefreshing = false
                        filledDashboardAdapter.submitList(event.dashboardItems)
                    }
                    is DashboardEvent.ShowErrorView -> {
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(
                            requireContext(),
                            event.cause.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DashboardEvent.ShowSwipeToRefreshView -> binding.swipeRefresh.isRefreshing =
                        true
                    is DashboardEvent.ShowRestoreItemSnackbar -> {
                        Snackbar.make(binding.recyclerDashboard,
                            R.string.lbl_item_removed,
                            Snackbar.LENGTH_LONG)
                            .setAction(R.string.lbl_undo) { viewModel.onRestoreItemClicked(event.item) }
                            .show()
                    }
                    is DashboardEvent.NavigateToSearchScreen -> {
                        val destination =
                            DashboardFragmentDirections.actionDashboardFragmentToSearchFragment()
                        findNavController().navigate(destination)
                    }
                    is DashboardEvent.NavigateToDetailScreen -> {
                        val destination =
                            DashboardFragmentDirections.actionDashboardFragmentToDetailFragment(
                                event.woeid,
                                event.city
                            )
                        findNavController().navigate(destination)
                    }
                }
            }
        }
    }

    private fun setupRecycler() {
        binding {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                recyclerDashboard.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                    if (scrollY > oldScrollY && btnSearch.isExtended)
                        btnSearch.shrink()

                    if (scrollY < oldScrollY && !btnSearch.isExtended)
                        btnSearch.extend()
                }
            } else {
                recyclerDashboard.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(view, dx, dy)
                        if (dy > 0 && btnSearch.isExtended)
                            btnSearch.shrink()

                        if (dy < 0 && !btnSearch.isExtended)
                            btnSearch.extend()
                    }
                })
            }

            val itemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            recyclerDashboard.addItemDecoration(itemDecoration)

            val headerDashboardAdapter = BaseAdapter(listOf(HeaderDashboard()))
            headerDashboardAdapter.submitList(listOf(DashboardHeader("Your today forecast")))

            val concatAdapter = ConcatAdapter(
                ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
                headerDashboardAdapter,
                filledDashboardAdapter
            )
            recyclerDashboard.adapter = concatAdapter

            recyclerDashboard.itemAnimator = null

            val swipeToDeleteCallback =
                SwipeToRemove(requireActivity().theme) { positionForRemove: Int ->
                    val itemForRemove =
                        filledDashboardAdapter.currentList[positionForRemove] as DashboardItem
                    viewModel.onDashboardSwiped(itemForRemove)
                }
            ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.recyclerDashboard)
        }
    }

    override fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentDashboardBinding {
        return FragmentDashboardBinding.inflate(inflater, container, false)
    }
}