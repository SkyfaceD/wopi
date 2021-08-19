package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.network.MetaWeatherApi
import javax.inject.Inject

class WeatherImpl @Inject constructor(
    private val metaWeatherApi: MetaWeatherApi,
) : Weather {
    override suspend fun searchByLocation(location: String): List<Search> {
        return metaWeatherApi.searchByLocation(location)
    }
}