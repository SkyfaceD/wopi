package org.skyfaced.wopi

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.skyfaced.wopi.model.response.SearchResponse
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.repository.search.SearchRepositoryImpl
import retrofit2.Retrofit

@ExperimentalSerializationApi
class MetaWeatherTest {
    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .build()

    private val weatherApi = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(MetaWeatherApi::class.java)

    private val weather = SearchRepositoryImpl(weatherApi)

    @After
    fun shutdownServer() {
        println("Now death")
        mockWebServer.shutdown()
    }

    @Test
    fun `Search Location With Query 'san'`() {
        mockWebServer.enqueueResponse("search-query-san.json", 200)

        runBlocking {
            val actual = weather.searchByLocation("san")
            val excepted = listOf(
                SearchResponse(
                    title = "San Francisco",
                    locationType = "City",
                    woeid = 2487956,
                    lattLong = "37.777119, -122.41964"
                ),
                SearchResponse(
                    title = "San Diego",
                    locationType = "City",
                    woeid = 2487889,
                    lattLong = "32.715691,-117.161720"
                ),
                SearchResponse(
                    title = "San Jose",
                    locationType = "City",
                    woeid = 2488042,
                    lattLong = "37.338581,-121.885567"
                ),
                SearchResponse(
                    title = "San Antonio",
                    locationType = "City",
                    woeid = 2487796,
                    lattLong = "29.424580,-98.494614"
                ),
                SearchResponse(
                    title = "Santa Cruz",
                    locationType = "City",
                    woeid = 2488853,
                    lattLong = "36.974018,-122.030952"
                ),
                SearchResponse(
                    title = "Santiago",
                    locationType = "City",
                    woeid = 349859,
                    lattLong = "-33.463039,-70.647942"
                ),
                SearchResponse(
                    title = "Santorini",
                    locationType = "City",
                    woeid = 56558361,
                    lattLong = "36.406651,25.456530"
                ),
                SearchResponse(
                    title = "Santander",
                    locationType = "City",
                    woeid = 773964,
                    lattLong = "43.461498,-3.810010"
                ),
                SearchResponse(
                    title = "Busan",
                    locationType = "City",
                    woeid = 1132447,
                    lattLong = "35.170429,128.999481"
                ),
                SearchResponse(
                    title = "Santa Cruz de Tenerife",
                    locationType = "City",
                    woeid = 773692,
                    lattLong = "28.46163,-16.267059"
                ),
                SearchResponse(
                    title = "Santa Fe",
                    locationType = "City",
                    woeid = 2488867,
                    lattLong = "35.666431,-105.972572"
                )
            )
            Assert.assertEquals(excepted, actual)
        }
    }
}