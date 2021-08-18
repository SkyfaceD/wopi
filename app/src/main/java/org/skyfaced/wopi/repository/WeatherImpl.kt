package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.network.WeatherApi
import javax.inject.Inject

class WeatherImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : Weather {
    override suspend fun searchByLocation(location: String): List<Search> {
        return weatherApi.searchByLocation(location)
    }
}