package org.skyfaced.wopi.repository.dasboard

import kotlinx.coroutines.flow.Flow
import org.skyfaced.wopi.model.adapter.DashboardItem

interface DashboardRepository {
    val dashboardItems: Flow<List<DashboardItem>>

    suspend fun addDashboardItem(dashboardItem: DashboardItem): Long

    suspend fun removeDashboardItem(dashboardItem: DashboardItem)
}