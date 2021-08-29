package org.skyfaced.wopi.network

import org.skyfaced.wopi.model.response.ConsolidatedWeather
import org.skyfaced.wopi.model.response.Location
import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.utils.result.Result
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetaWeatherApi {
    @GET("/api/location/search/")
    suspend fun searchByLocation(@Query("query") location: String): Result<List<Search>>

    @GET("/api/location/search/")
    suspend fun searchByCoordinates(@Query("lattlong") lattLong: String): Result<List<Search>>

    @GET("/api/location/{woeid}/")
    suspend fun getLocation(@Path("woeid") woeid: Int): Result<Location>

    /**
     * @param date should be in that pattern 'yyyy/MM/dd'
     */
    @GET("/api/location/{woeid}/{date}/")
    suspend fun getConsolidatedWeatherByDate(
        @Path("woeid") woeid: Int,
        @Path("date", encoded = true) date: String,
    ): List<ConsolidatedWeather>

    @GET("/api/location/{woeid}/{year}/{month}/{day}/")
    suspend fun getConsolidatedWeatherByDate(
        @Path("woeid") woeid: Int,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
    ): List<ConsolidatedWeather>
}