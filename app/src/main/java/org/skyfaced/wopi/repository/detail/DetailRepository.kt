package org.skyfaced.wopi.repository.detail

import kotlinx.coroutines.flow.Flow
import org.skyfaced.wopi.database.entity.DashboardEntity
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.utils.State

interface DetailRepository {
    fun getDetails(woeid: Int): Flow<State<List<DetailItem>>>

    suspend fun isFavorite(woeid: Int): Boolean

    suspend fun addToFavorite(entity: DashboardEntity): Long

    suspend fun removeFromFavorite(woeid: Int)
}