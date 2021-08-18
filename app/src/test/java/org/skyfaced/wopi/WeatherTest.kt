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
import org.skyfaced.wopi.model.response.Search
import org.skyfaced.wopi.network.WeatherApi
import org.skyfaced.wopi.repository.WeatherImpl
import retrofit2.Retrofit

@ExperimentalSerializationApi
class WeatherTest {
    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .build()

    private val weatherApi = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(WeatherApi::class.java)

    private val weather = WeatherImpl(weatherApi)

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
                Search(
                    title = "San Francisco",
                    locationType = "City",
                    woeid = 2487956,
                    lattLong = "37.777119, -122.41964"
                ),
                Search(
                    title = "San Diego",
                    locationType = "City",
                    woeid = 2487889,
                    lattLong = "32.715691,-117.161720"
                ),
                Search(
                    title = "San Jose",
                    locationType = "City",
                    woeid = 2488042,
                    lattLong = "37.338581,-121.885567"
                ),
                Search(
                    title = "San Antonio",
                    locationType = "City",
                    woeid = 2487796,
                    lattLong = "29.424580,-98.494614"
                ),
                Search(
                    title = "Santa Cruz",
                    locationType = "City",
                    woeid = 2488853,
                    lattLong = "36.974018,-122.030952"
                ),
                Search(
                    title = "Santiago",
                    locationType = "City",
                    woeid = 349859,
                    lattLong = "-33.463039,-70.647942"
                ),
                Search(
                    title = "Santorini",
                    locationType = "City",
                    woeid = 56558361,
                    lattLong = "36.406651,25.456530"
                ),
                Search(
                    title = "Santander",
                    locationType = "City",
                    woeid = 773964,
                    lattLong = "43.461498,-3.810010"
                ),
                Search(
                    title = "Busan",
                    locationType = "City",
                    woeid = 1132447,
                    lattLong = "35.170429,128.999481"
                ),
                Search(
                    title = "Santa Cruz de Tenerife",
                    locationType = "City",
                    woeid = 773692,
                    lattLong = "28.46163,-16.267059"
                ),
                Search(
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