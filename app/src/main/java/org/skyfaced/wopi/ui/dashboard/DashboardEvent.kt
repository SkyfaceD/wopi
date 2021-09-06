package org.skyfaced.wopi.ui.dashboard

import org.skyfaced.wopi.model.adapter.DashboardItem

//TODO Experimental one
sealed class DashboardEvent {
    data class PopulateDashboardItems(val dashboardItems: List<DashboardItem>) : DashboardEvent()
    data class ShowErrorView(val cause: Throwable) : DashboardEvent()
    object ShowSwipeToRefreshView : DashboardEvent()
    data class ShowRestoreItemSnackbar(val item: DashboardItem) : DashboardEvent()

    object NavigateToSearchScreen : DashboardEvent()
    data class NavigateToDetailScreen(val woeid: Int, val city: String) : DashboardEvent()
}