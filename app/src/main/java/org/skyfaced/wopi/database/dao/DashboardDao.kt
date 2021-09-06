package org.skyfaced.wopi.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.skyfaced.wopi.database.BaseDao
import org.skyfaced.wopi.database.entity.DashboardEntity

@Dao
abstract class DashboardDao : BaseDao<DashboardEntity> {
    @Query("SELECT * FROM dashboard")
    abstract fun getAllAsFlow(): Flow<List<DashboardEntity>>

    @Query("SELECT COUNT(woeid) FROM dashboard WHERE woeid = :woeid")
    abstract suspend fun getCountById(woeid: Int): Int

    @Query("DELETE FROM dashboard WHERE woeid = :woeid")
    abstract suspend fun deleteById(woeid: Int)

    @Query("DELETE FROM dashboard")
    abstract suspend fun deleteAll()
}