package org.skyfaced.wopi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsolidatedWeatherResponse(
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