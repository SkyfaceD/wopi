package org.skyfaced.wopi.repository.dasboard

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.skyfaced.wopi.database.dao.DashboardDao
import org.skyfaced.wopi.database.entity.DashboardEntity
import org.skyfaced.wopi.model.adapter.DashboardItem
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardDao: DashboardDao,
    ioDispatcher: CoroutineDispatcher,
) : DashboardRepository {
    override val dashboardItems: Flow<List<DashboardItem>> = dashboardDao.getAllAsFlow()
        .map { it.map(DashboardEntity::toDashboardItem) }
        .flowOn(ioDispatcher)

    override suspend fun addDashboardItem(dashboardItem: DashboardItem): Long {
        return dashboardDao.insert(dashboardItem.toDashboardEntity())
    }

    override suspend fun removeDashboardItem(dashboardItem: DashboardItem) {
        dashboardDao.delete(dashboardItem.toDashboardEntity())
    }
}