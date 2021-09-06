package org.skyfaced.wopi.database.converter

import androidx.room.TypeConverter
import org.skyfaced.wopi.model.Temperature

class TemperatureConverter {
    @TypeConverter
    fun fromTemperature(temperature: Temperature): Double = temperature.centigrade

    @TypeConverter
    fun toTemperature(centigrade: Double): Temperature = Temperature(centigrade)
}