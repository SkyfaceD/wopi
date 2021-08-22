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
    suspend fun location(@Path("woeid") woeid: String): Location

    @GET("/api/location/{woeid}/(date)/")
    suspend fun locationDate(
        @Path("woeid") woeid: String,
        @Path("date") date: String,
    ): List<ConsolidatedWeather>

    @GET("/static/img/weather/{abbreviation}.svg")
    suspend fun svg(@Path("abbreviation") abbreviation: String)
}