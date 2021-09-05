package org.skyfaced.wopi.network

import org.skyfaced.wopi.model.response.ConsolidatedWeatherResponse
import org.skyfaced.wopi.model.response.LocationResponse
import org.skyfaced.wopi.model.response.SearchResponse
import org.skyfaced.wopi.utils.result.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetaWeatherApi {
    @GET("/api/location/search/")
    suspend fun searchByLocation(@Query("query") location: String): Result<List<SearchResponse>>

    @GET("/api/location/search/")
    suspend fun searchByCoordinates(@Query("lattlong") lattLong: String): Result<List<SearchResponse>>

    @GET("/api/location/{woeid}/")
    suspend fun getLocation(@Path("woeid") woeid: Int): Result<LocationResponse>

    /**
     * @param date should be in that pattern 'yyyy/MM/dd'
     */
    @GET("/api/location/{woeid}/{date}/")
    suspend fun getConsolidatedWeathersByDate(
        @Path("woeid") woeid: Int,
        @Path("date", encoded = true) date: String,
    ): Result<List<ConsolidatedWeatherResponse>>
}