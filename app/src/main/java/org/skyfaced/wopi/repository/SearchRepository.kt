package org.skyfaced.wopi.repository

import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.utils.result.Result

interface SearchRepository {
    suspend fun searchByLocation(location: String): Result<List<Search>>

    suspend fun searchByCoordinates(coordinates: String): Result<List<Search>>
}