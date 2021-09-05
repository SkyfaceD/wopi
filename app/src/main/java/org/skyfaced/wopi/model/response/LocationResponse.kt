package org.skyfaced.wopi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val woeid: Int,
    val title: String,
    @SerialName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeatherResponse>,
    @SerialName("latt_long")
    val lattLong: String,
    @SerialName("location_type")
    val locationType: String,
    val parent: SearchResponse,
    @SerialName("sun_rise")
    val sunRise: String,
    @SerialName("sun_set")
    val sunSet: String,
    val time: String,
    val timezone: String,
    @SerialName("timezone_name")
    val timezoneName: String,
)