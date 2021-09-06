package org.skyfaced.wopi.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.skyfaced.wopi.model.adapter.DashboardItem
import org.skyfaced.wopi.repository.dasboard.DashboardRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {
    private val _dashboardEvent = MutableSharedFlow<DashboardEvent>()
    val dashboardEvent = _dashboardEvent.asSharedFlow()

    init {
        retry()
    }

    fun retry() {
        viewModelScope.launch {
            fetchDashboardItems()
        }
    }

    fun onDetailItemClicked(item: DashboardItem) = viewModelScope.launch {
        _dashboardEvent.emit(DashboardEvent.NavigateToDetailScreen(item.woeid, item.city))
    }

    fun onSearchClicked() = viewModelScope.launch {
        _dashboardEvent.emit(DashboardEvent.NavigateToSearchScreen)
    }

    fun onDashboardSwiped(item: DashboardItem) = viewModelScope.launch {
        dashboardRepository.removeDashboardItem(item)
        _dashboardEvent.emit(DashboardEvent.ShowRestoreItemSnackbar(item))
    }

    fun onRestoreItemClicked(item: DashboardItem) = viewModelScope.launch {
        dashboardRepository.addDashboardItem(item)
        fetchDashboardItems()
    }

    private suspend fun fetchDashboardItems() {
        dashboardRepository.dashboardItems
            .onStart { DashboardEvent.ShowSwipeToRefreshView }
            .catch { _dashboardEvent.emit(DashboardEvent.ShowErrorView(it)) }
            .collect { _dashboardEvent.emit(DashboardEvent.PopulateDashboardItems(it)) }
    }
}