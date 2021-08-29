package org.skyfaced.wopi.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.skyfaced.wopi.model.response.ConsolidatedWeather
import org.skyfaced.wopi.model.response.Location
import org.skyfaced.wopi.network.MetaWeatherApi
import org.skyfaced.wopi.utils.Temperature
import org.skyfaced.wopi.utils.WeatherState
import org.skyfaced.wopi.utils.extensions.round
import org.skyfaced.wopi.utils.result.Result
import org.skyfaced.wopi.utils.result.asSuccess
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val metaWeatherApi: MetaWeatherApi,
) : DetailRepository {
    override suspend fun fetchLocation(woeid: Int): Result<Location> {
        return metaWeatherApi.getLocation(woeid)
    }

    data class Detail(
        val general: General,
        val today: List<Today>,
        val week: List<Week>,
    ) {
        /**
         * @param temperature example - 23°C or 23 K or 23°F
         * @param date example - Wednesday, 25 August
         * @param wind example - 5 km/h\nWind
         * @param pressure example - 1022 mmHg\nPressure
         * @param humidity example - 40%\nHumidity
         */
        data class General(
            val city: String,
            val weatherState: WeatherState,
            val temperature: String,
            val date: String,
            val wind: String,
            val pressure: String,
            val humidity: String,
        )

        /**
         * @param temperature example - 23°C or 23 K or 23°F
         * @param time example - 10:00
         */
        data class Today(
            val temperature: String,
            val weatherState: WeatherState,
            val time: String,
        )

        /**
         * @param weekOfDay example - Mon, Tue, Wed and so on
         * @param temperature example - +19°C +20°C
         */
        data class Week(
            val weekOfDay: String,
            val weatherState: WeatherState,
            val temperature: String,
        )
    }

    suspend fun fetch(woeid: Int): Flow<Detail> {
        Timber.d("Fetch: $woeid")
        return flow {
            val (location, todays) = coroutineScope {
                val location = async(Dispatchers.IO) {
                    metaWeatherApi.getLocation(woeid).asSuccess().value
                }

                val dtf = DateTimeFormat.forPattern("yyyy/MM/dd")
                val today = dtf.print(DateTime.now())
                val consolidatedWeathers = async(Dispatchers.IO) {
                    metaWeatherApi.getConsolidatedWeatherByDate(
                        woeid,
                        today
                    )
                }

                location.await() to
                        consolidatedWeathers.await().filter {
                            Timber.e("ServerDate ${
                                DateTime.parse(it.created).dayOfYear()
                            }\nCurrDate ${DateTime.now().dayOfYear()}")

                            DateTime.parse(it.created).dayOfYear() == DateTime.now().dayOfYear()
                        }
            }

            Timber.d("Location: $location")

            //General
            val latestForecast = location.consolidatedWeather[0]
            val weatherState = WeatherState.fromAbbreviation(latestForecast.weatherStateAbbr)
            val temperature = Temperature(latestForecast.theTemp.round())
            val date = SimpleDateFormat("EEEE, dd MMMM").format(
                SimpleDateFormat("yyyy-MM-dd").parse(latestForecast.applicableDate)
            )
            //FIXME language
            val wind = "${latestForecast.windSpeed.round(2)} km/h\nWind"
            val pressure = "${latestForecast.airPressure} mmHg\nPressure"
            val humidity = "${latestForecast.humidity}%\nHumidity"
            val general = Detail.General(
                city = location.title,
                weatherState = weatherState,
                temperature = temperature.celsius,
                date = date,
                wind = wind,
                pressure = pressure,
                humidity = humidity
            )

            //Today
            val detailToday = mutableListOf<Detail.Today>()
            for (today in todays) {
                val todayTemperature = Temperature(today.theTemp.round(2))
                val todayWeatherState = WeatherState.fromAbbreviation(today.weatherStateAbbr)
                val dtf = DateTimeFormat.forPattern("HH:mm")
                val todayTime = dtf.print(DateTime.parse(today.created))
                detailToday.add(
                    Detail.Today(
                        temperature = todayTemperature.celsius,
                        weatherState = todayWeatherState,
                        time = todayTime
                    )
                )
            }

            //Week
            val detailWeek = mutableListOf<Detail.Week>()
            for (week in location.consolidatedWeather) {
                val weekOfDay = SimpleDateFormat("EEE").format(
                    SimpleDateFormat("yyyy-MM-dd").parse(week.applicableDate)
                )
                val weekWeatherState = WeatherState.fromAbbreviation(week.weatherStateAbbr)
                val weekTemperature = Temperature(week.theTemp.round(2))
                detailWeek.add(
                    Detail.Week(
                        weekOfDay = weekOfDay,
                        weatherState = weekWeatherState,
                        temperature = weekTemperature.celsius
                    )
                )
            }

            val detail = Detail(
                general = general,
                today = detailToday,
                week = detailWeek
            )

            emit(detail)
        }
    }

    override suspend fun fetchTodayForecast(woeid: Int): List<ConsolidatedWeather> {
        val today = SimpleDateFormat("yyyy/MM/dd").format(Date(System.currentTimeMillis()))
        return metaWeatherApi.getConsolidatedWeatherByDate(woeid, today)
    }

    override suspend fun fetchForecastForDate(woeid: Int, date: String): List<ConsolidatedWeather> {
        return metaWeatherApi.getConsolidatedWeatherByDate(woeid, date)
    }
}