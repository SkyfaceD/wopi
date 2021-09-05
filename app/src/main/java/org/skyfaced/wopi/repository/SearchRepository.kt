package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.SearchResponse
import org.skyfaced.wopi.utils.result.Result

interface SearchRepository {
    suspend fun searchByLocation(location: String): Result<List<SearchResponse>>

    suspend fun searchByCoordinates(coordinates: String): Result<List<SearchResponse>>
}