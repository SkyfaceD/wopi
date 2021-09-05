package org.skyfaced.wopi.repository.search

import org.skyfaced.wopi.model.response.SearchResponse
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.utils.result.Result
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val metaWeatherApi: MetaWeatherApi,
) : SearchRepository {
    override suspend fun searchByLocation(location: String): Result<List<SearchResponse>> {
        return metaWeatherApi.searchByLocation(location)
    }

    override suspend fun searchByCoordinates(coordinates: String): Result<List<SearchResponse>> {
        return metaWeatherApi.searchByCoordinates(coordinates)
    }
}