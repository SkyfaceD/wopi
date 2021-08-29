package org.skyfaced.wopi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val woeid: Int,
    val title: String,
    @SerialName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeather>,
    @SerialName("latt_long")
    val lattLong: String,
    @SerialName("location_type")
    val locationType: String,
    val parent: Search,
    val sources: List<Source>,
    @SerialName("sun_rise")
    val sunRise: String,
    @SerialName("sun_set")
    val sunSet: String,
    val time: String,
    val timezone: String,
    @SerialName("timezone_name")
    val timezoneName: String,
)

@Serializable
data class ConsolidatedWeather(
    val id: Long,
    @SerialName("air_pressure")
    val airPressure: Double,
    @SerialName("applicable_date")
    val applicableDate: String,
    val created: String,
    val humidity: Int,
    @SerialName("max_temp")
    val maxTemp: Double,
    @SerialName("min_temp")
    val minTemp: Double,
    val predictability: Int,
    @SerialName("the_temp")
    val theTemp: Double,
    val visibility: Double?,
    @SerialName("weather_state_abbr")
    val weatherStateAbbr: String,
    @SerialName("weather_state_name")
    val weatherStateName: String,
    @SerialName("wind_direction")
    val windDirection: Double,
    @SerialName("wind_direction_compass")
    val windDirectionCompass: String,
    @SerialName("wind_speed")
    val windSpeed: Double,
)

@Serializable
data class Source(
    @SerialName("crawl_rate")
    val crawlRate: Int,
    val slug: String,
    val title: String,
    val url: String,
)