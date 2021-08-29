package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.ConsolidatedWeather
import org.skyfaced.wopi.model.response.Location
import org.skyfaced.wopi.utils.result.Result

interface DetailRepository {
    suspend fun fetchLocation(woeid: Int): Result<Location>

    suspend fun fetchTodayForecast(woeid: Int): List<ConsolidatedWeather>

    suspend fun fetchForecastForDate(woeid: Int, date: String): List<ConsolidatedWeather>
}