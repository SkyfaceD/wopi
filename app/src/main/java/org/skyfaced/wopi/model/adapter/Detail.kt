package org.skyfaced.wopi.model.adapter

import org.joda.time.DateTime
import org.skyfaced.wopi.model.WeatherState
import org.skyfaced.wopi.utils.extensions.lazySafetyNone
import org.skyfaced.wopi.utils.extensions.round
import java.util.*
import kotlin.math.roundToInt

data class DetailItem(
    val weatherState: WeatherState,
    val date: DateTime,
    val helper: DetailHelper,
    val week: Week = Week(date.toString("EEE", Locale.ENGLISH)),
) {
    val dateStr: String by lazySafetyNone { date.toString("EEEE, dd MMMM", Locale.ENGLISH) }
}

data class Week(
    val dayOfWeek: String,
    val isChecked: Boolean = false,
)

/**
 * @param temperature default in centigrade
 * @param windSpeed default in mph
 * @param visibility default in miles
 *
 * Property result examples:
 * @property celsius - '23℃'
 * @property fahrenheit - '73.4℉'
 * @property kelvin - '296.15K'
 * @property windMph - '3 mph'
 * @property windKph - '6 kph'
 * @property airPressureStr - '1022 mbar'
 * @property humidityStr - '40%'
 * @property visibilityMiles - '18.4 miles'
 * @property visibilityKilometers - '29.6 kilometers'
 */
class DetailHelper(
    temperature: Double,
    windSpeed: Double,
    airPressure: Double,
    humidity: Int,
    visibility: Double,
) {
    val celsius by lazySafetyNone { "${temperature.roundToInt()}℃" }
    val fahrenheit by lazySafetyNone { "${((temperature * 9 / 5) + 32).roundToInt()}℉" }
    val kelvin by lazySafetyNone { "${(temperature + 273.15).roundToInt()}K" }

    val windMph by lazySafetyNone { "${windSpeed.roundToInt()} mph" }
    val windKph by lazySafetyNone { "${windSpeed / 1.60934} kph" }

    val airPressureStr = "${airPressure.roundToInt()} mbar"

    val humidityStr = "$humidity%"

    val visibilityMiles by lazySafetyNone { "${visibility.round(1)} miles" }
    val visibilityKilometers by lazySafetyNone { "${(visibility / 1.60934).round(1)} kilometers" }
}