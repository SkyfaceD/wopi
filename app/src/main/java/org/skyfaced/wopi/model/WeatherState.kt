package org.skyfaced.wopi.model

import androidx.annotation.DrawableRes
import org.skyfaced.wopi.R

enum class WeatherState(
    val abbreviation: String,
    @DrawableRes
    val drawableRes: Int,
    val value: String,
) {
    Snow("sn", R.drawable.weather_state_snow, "Snow"),
    Sleet("sl", R.drawable.weather_state_sleet, "Sleet"),
    Hail("h", R.drawable.weather_state_hail, "Hail"),
    Thunderstorm("t", R.drawable.weather_state_thunderstorm, "Thunderstorm"),
    HeavyRain("hr", R.drawable.weather_state_heavy_rain, "Heavy Rain"),
    LightRain("lr", R.drawable.weather_state_light_rain, "Light Rain"),
    Showers("s", R.drawable.weather_state_showers, "Showers"),
    HeavyCloud("hc", R.drawable.weather_state_heavy_cloud, "Heavy Cloud"),
    LightCloud("lc", R.drawable.weather_state_light_cloud, "Light Cloud"),
    Clear("c", R.drawable.weather_state_clear, "Clear");

    companion object {
        fun fromAbbreviation(abbreviation: String): WeatherState {
            return values().find { it.abbreviation == abbreviation }
                ?: throw IllegalArgumentException(
                    "Unexpected abbreviation: $abbreviation"
                )
        }
    }
}