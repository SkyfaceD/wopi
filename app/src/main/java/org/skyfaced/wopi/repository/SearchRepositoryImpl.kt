package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.utils.result.Result
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val metaWeatherApi: MetaWeatherApi,
) : SearchRepository {
    override suspend fun searchByLocation(location: String): Result<List<Search>> {
        return metaWeatherApi.searchByLocation(location)
    }

    override suspend fun searchByCoordinates(coordinates: String): Result<List<Search>> {
        return metaWeatherApi.searchByCoordinates(coordinates)
    }
}