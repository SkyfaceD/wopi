package org.skyfaced.wopi.repository.detail

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.joda.time.DateTime
import org.skyfaced.wopi.database.dao.DashboardDao
import org.skyfaced.wopi.database.entity.DashboardEntity
import org.skyfaced.wopi.model.Temperature
import org.skyfaced.wopi.model.WeatherState
import org.skyfaced.wopi.model.adapter.DetailHelper
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.utils.State
import org.skyfaced.wopi.utils.extensions.onResponse
import org.skyfaced.wopi.utils.extensions.success
import org.skyfaced.wopi.utils.result.asSuccess
import org.skyfaced.wopi.utils.result.isFailure
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val metaWeatherApi: MetaWeatherApi,
    private val dashboardDao: DashboardDao,
) : DetailRepository {
    override fun getDetails(woeid: Int): Flow<State<List<DetailItem>>> {
        return flow<State<List<DetailItem>>> {
            val result = metaWeatherApi.getLocation(woeid)

            if (result.isFailure()) {
                throw result.error
            }

            val details = mutableListOf<DetailItem>()
            for (response in result.asSuccess().value.consolidatedWeather) {
                details.add(
                    DetailItem(
                        weatherState = WeatherState.fromAbbreviation(response.weatherStateAbbr),
                        date = DateTime.parse(response.applicableDate),
                        helper = DetailHelper(
                            temperature = Temperature(response.theTemp),
                            windSpeed = response.windSpeed,
                            airPressure = response.airPressure,
                            humidity = response.humidity,
                            visibility = response.visibility ?: 0.0
                        )
                    )
                )
            }

            emit(success(details))
        }.onResponse().flowOn(Dispatchers.IO)
    }

    /**
     * @return true if forecast already in favorite, false otherwise
     */
    override suspend fun isFavorite(woeid: Int): Boolean {
        return dashboardDao.getCountById(woeid) > 0
    }

    override suspend fun addToFavorite(entity: DashboardEntity): Long {
        return dashboardDao.insert(entity)
    }

    override suspend fun removeFromFavorite(woeid: Int) {
        dashboardDao.deleteById(woeid)
    }
}