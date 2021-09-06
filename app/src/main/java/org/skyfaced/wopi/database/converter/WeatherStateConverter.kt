package org.skyfaced.wopi.database.converter

import androidx.room.TypeConverter
import org.skyfaced.wopi.model.WeatherState

class WeatherStateConverter {
    @TypeConverter
    fun fromWeatherState(weatherState: WeatherState): String = weatherState.name

    @TypeConverter
    fun toWeatherState(name: String): WeatherState = WeatherState.valueOf(name)
}