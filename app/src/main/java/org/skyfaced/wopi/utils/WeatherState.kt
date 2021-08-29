package org.skyfaced.wopi.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.skyfaced.wopi.R

enum class WeatherState(
    val abbreviation: String,
    @DrawableRes
    val drawableRes: Int,
    @StringRes
    val stringRes: Int,
) {
    Snow("sn", R.drawable.weather_state_snow, R.string.weather_state_snow),
    Sleet("sl", R.drawable.weather_state_sleet, R.string.weather_state_sleet),
    Hail("h", R.drawable.weather_state_hail, R.string.weather_state_hail),
    Thunderstorm("t", R.drawable.weather_state_thunderstorm, R.string.weather_state_thunderstorm),
    HeavyRain("hr", R.drawable.weather_state_heavy_rain, R.string.weather_state_heavy_rain),
    LightRain("lr", R.drawable.weather_state_light_rain, R.string.weather_state_light_rain),
    Showers("s", R.drawable.weather_state_showers, R.string.weather_state_showers),
    HeavyCloud("hc", R.drawable.weather_state_heavy_cloud, R.string.weather_state_heavy_cloud),
    LightCloud("lc", R.drawable.weather_state_light_cloud, R.string.weather_state_light_cloud),
    Clear("c", R.drawable.weather_state_clear, R.string.weather_state_clear);

    companion object {
        fun fromAbbreviation(abbreviation: String): WeatherState {
            return values().find { it.abbreviation == abbreviation }
                ?: throw IllegalArgumentException(
                    "Unexpected abbreviation: $abbreviation"
                )
        }
    }
}